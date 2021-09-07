import React, { useState, useReducer } from 'react';
import { Typography, Table, Button, Checkbox, Divider, Popconfirm } from 'antd';
import axios from "axios";
import qs from 'qs'

import { apiUrl } from "../../helper/url";
import CodeSelection from '../codeSelection/CodeSelection';

const CheckboxGroup = Checkbox.Group;
const { Title } = Typography;

const plainOptions = ['Anlagengröße', 'Nutzungsgrad', 'Temperaturdifferenz', 'Rücklauftemperatur'];
const defaultCheckedList = ['Anlagengröße', 'Nutzungsgrad', 'Temperaturdifferenz', 'Rücklauftemperatur'];

const ConfigurationModule = () => {
  const [dataSource, setDataSource] = useState([]);
  const [value, setValue] = useState(['0-0-0', '0-0-1']); // TODO: change default to []
  const [rowSelection, setRowSelection] = useState([])
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
    const codes =  ["ACO.001", "ACO.002"]; // TODO: get codes dynamically, eg. through values, or through CodeSelection props
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
        setDataSource(Object.keys(data).map(key => {
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
        }))
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
        <CodeSelection value={value} setValue={setValue} /> 
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
