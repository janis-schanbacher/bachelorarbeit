import React, { useState, useReducer } from 'react';
import { Typography, Table, Button, Checkbox, Divider } from 'antd';
import axios from "axios";
import qs from 'qs'
import { find, findIndex } from 'lodash';

import { apiUrl } from "../../helper/url";
import CodeSelection from '../codeSelection/CodeSelection';

const CheckboxGroup = Checkbox.Group;
const { Title } = Typography;

const plainOptions = ['Anlagengröße', 'Nutzungsgrad', 'Temperaturdifferenz', 'Rücklauftemperatur'];
const defaultCheckedList = ['Anlagengröße', 'Nutzungsgrad', 'Temperaturdifferenz', 'Rücklauftemperatur'];

const ConfigurationModule = () => {
  const [dataSource, setDataSource] = useState([]);
  const [value, setValue] = useState([]); // TODO: change default to []
  const [rowSelection, setRowSelection] = useState([])
  const [treeData, setTreeData] = useState([])
  // eslint-disable-next-line no-unused-vars
  const [_, forceUpdate] = useReducer((x) => x + 1, 0); // TODO: remove

  const handleChange = (code, list) => {
    const index = dataSource.findIndex((item) => item.key === code); 
    const newDataSource = dataSource;
    newDataSource[index].checkedList = list   
    setDataSource(newDataSource)
    forceUpdate(); // TODO: remove
  };

  // TODO: evt. auch einfach alle anlagen darstellen, und nach coedes gruppieren, die dann ausklappbar sind. https://ant.design/components/table/#components-table-demo-tree-data 
  const columns = [
    { title: "Anlagencode",
      dataIndex: "code",
    },
    {
      title: 'Aktive Analysen',
      dataIndex: 'activeAnalyses',
      width: '60%',
      // editable: true,
      render: (val, row) => {    
        const index = dataSource.findIndex((item) => item.key === row.key); 
        return <CheckboxGroup options={plainOptions} value={dataSource[index].checkedList} onChange={(list) => handleChange(row.key, list)} />
      }
    },
    {
      title: 'Bestätigen',
      dataIndex: 'confirm',
      render: (_, record) =>
        dataSource.length >= 1 ? (
          <Button type="primary" onClick={(e) => handleConfirm(record)}>Anwenden</Button>
        ) : null,
    },
  ];

  const loadConfigs = () => {
    console.log(value);
    const codes = value.filter(v => v.value.length > 3).map(v => v.label)
    // Add codes of children from selected prefix (f.i. {value: "0-1", value: "ACO"})
    const keysOfPrefixes = value.filter(v => v.value.length === 3)
    for(var i = 0; i < keysOfPrefixes.length ; i++) {
      const treeElement = find(treeData, o => o.value === keysOfPrefixes[i].value)
      console.log("treeElement: " + treeElement);
      if(treeElement !== undefined && treeElement.children !== undefined){
        console.log(treeElement);
        for(var k = 0; k < treeElement.children.length ; k++){
          codes.push(treeElement.children[k].title);
        } 
      }
    }
      
    // retrieve configs from backend
    axios.get(`${apiUrl}/configs/get-list`, {
          params: {
            codes: codes
          },
          paramsSerializer: params => {
            return qs.stringify(params, {arrayFormat: 'repeat'})
          }
        })
    .then((res) => {
        const { data } = res;
        console.log(rowSelection);
        const configs = Object.keys(data).map(key => {
            var activeAnalyses = [];
            if(data[key].facilitySize){
                activeAnalyses.push('Anlagengröße')
            }
            if(data[key].utilizationRate){
                activeAnalyses.push('Nutzungsgrad');
            }
            if(data[key].deltaTemperature){
                activeAnalyses.push('Temperaturdifferenz');
            }
            if(data[key].returnTemperature){
                activeAnalyses.push('Rücklauftemperatur');
            }
            return {
                key: data[key].id, 
                code: data[key].id.toUpperCase(), 
                checkedList: activeAnalyses
            }
        })

        // add default config, if non exists yet
        // const keysOfRetrievedConfigs = configs.map(o => o.key)
        // console.log("keysOfRetrievedConfigs: " + keysOfRetrievedConfigs);
        for(var i = 0; i < codes.length; i++) {
          console.log(configs);
          const index =  configs.findIndex(c => c.code === codes[i].toUpperCase()) 
          console.log("index: " + index);
          if(index < 0){ 
            console.log("Adding default for: " + codes[i]);
            configs.push({key: codes[i], code: codes[i], checkedList: defaultCheckedList})
          }
        }

        setDataSource(configs)
    }).catch((err) => {
      console.log(err)
    });
  }

  const handleConfirm = (record) => {
    axios.post(`${apiUrl}/configs`, {
        "id": record.key,
        "facilitySize": record.checkedList.includes("Anlagengröße"),
        "utilizationRate": record.checkedList.includes("Nutzungsgrad"),
        "deltaTemperature": record.checkedList.includes("Temperaturdifferenz"),
        "returnTemperature": record.checkedList.includes("Rücklauftemperatur")
    })
  };
  
  const handleConfirmSelection = () => {
    // TODO:"bundle as one request and create api endpoint"
    for(const record of rowSelection) {
        handleConfirm(record)
    }
  };

  const columnsRender = columns.map((col) => {
    if (!col.editable) {
      return col;
    }

    return {
    ...col,
    onCell: (record) => ({
      record,
      editable: col.editable,
      dataIndex: col.dataIndex,
      title: col.title,
      handleSave: handleConfirm,
    }),
    };
  });

  const customRowSelection = {
    onChange: (selectedRowKeys, selectedRows) => {
        setRowSelection(selectedRows);
        console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
    },
    getCheckboxProps: (record) => ({
      disabled: record.name === 'Disabled User',
      // Column configuration not to be checked
      name: record.name,
    }),
  };

  return(
    <div>
        <Title level={2}>Konfigurations-Oberfläche</Title>
        <Title level={4} style={{textAlign: "left"}}>Auswahl zu konfigurierender Anlagen</Title>
        <CodeSelection value={value} setValue={setValue} treeData={treeData} setTreeData={setTreeData} /> 
        <Button
          onClick={loadConfigs}
          type="primary"
          style={{
              margin: "5px 5px 15px 5px",
              float: 'left'
          }} // TODO: use StyledComponend
        >
        Konfigurationen laden
        </Button>
        <Divider />  
        <Title level={4} style={{textAlign: "left"}}>Konfigurationen</Title>
        <Table
         rowSelection={{
          type: "checkbox",
          ...customRowSelection,
        }}
          rowClassName={() => 'editable-row'}
          bordered
          dataSource={dataSource}
          columns={columnsRender}
        />
        <Button
          onClick={handleConfirmSelection}
          type="primary"
          style={{
              margin: "5px 5px 15px 5px",
              float: 'left'
          }} // TODO: use StyledComponend
         >Auswahl anwenden</Button>
    </div>
  );
}

export default ConfigurationModule;
