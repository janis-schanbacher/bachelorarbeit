import React, { useState, useReducer } from 'react';
import { Card, Table, Button, Checkbox, Divider, Popconfirm } from 'antd';
import axios from "axios";
import qs from 'qs'

import { apiUrl } from "../../helper/url";
import CodeSelection from '../codeSelection/CodeSelection';

const CheckboxGroup = Checkbox.Group;

const plainOptions = ['Anlagengröße', 'Nutzungsgrad', 'Temperaturdifferenz', 'Rücklauftemperatur'];
const defaultCheckedList = ['Anlagengröße', 'Nutzungsgrad', 'Temperaturdifferenz', 'Rücklauftemperatur'];

const ConfigurationModule = () => {
  const [dataSource, setDataSource] = useState([]);
  const [originalDataSource, setOriginalDataSource] = useState([]);
  const [value, setValue] = useState(['0-0-0', '0-0-1']); // TODO: change default to []
  const [rowSelection, setRowSelection] = useState([])

  const [checkedList, setCheckedList] = useState(defaultCheckedList);
  const [indeterminate, setIndeterminate] = useState(true);
  const [checkAll, setCheckAll] = useState(false);

  const [_, forceUpdate] = useReducer((x) => x + 1, 0); // TODO: remove

  const handleChange = (code, list) => {
    const index = dataSource.findIndex((item) => item.key === code); 
    const newDataSource = dataSource;
    newDataSource[index].checkedList = list   
    setDataSource(newDataSource)
    forceUpdate(); // TODO: remove
  };

  const handleChangeAll = (list) => {
      // TODO: request /config codes, list
      setCheckedList(list)

  }
//   const handleChange = (e) => {
//     const code = e.target.attributes.code.nodeValue;
//     console.log(code);
//     const newDataSource = dataSource;
//     var index = -1;
//     for (var i = 0; i < dataSource.length; i += 1) {
//       if (dataSource[i]["code"] === code) {
//           index = i;
//           break;
//       }
//     }
//     console.log(e.target.value);
//     newDataSource[index]["textFragments"] = e.target.value;
//     setDataSource(newDataSource);
//   }

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
          // <Popconfirm title="Sure to save?" onConfirm={() => handleSave(record.key)}>
          //   <a>Speichern</a>
          // </Popconfirm>
          <Button type="primary" onClick={(e) => handleConfirm(record)}>Anwenden</Button>
        ) : null,
    },
  ];

  // TODO: rewrite to getConfigs
  const loadConfigs = () => {
    const codes =  ["ACO.001", "ACO.002"];
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
    })
    .then(() => {
      setOriginalDataSource(dataSource);
    })
    .catch((err) => {
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

  // TODO: handleConfirmSelected. handleChangeAll
  const handleSaveAll = () => {
    console.log(rowSelection);
    // console.log(selectedRowKeys);
    // const newData = dataSource;
    // const index = newData.findIndex((item) => row.key === item.key);
    // const item = newData[index];
    // newData.splice(index, 1, { ...item, ...row });
    // setDataSource(newData);
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
      <h1>Konfigurations-Oberfläche</h1>
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
        <Card>
        <h3 style={{textAlign: 'left'}}>Konfiguration für alle markierten Anlagen anpassen:</h3>
        <CheckboxGroup options={plainOptions} value={checkedList} onChange={(list) => handleChangeAll(list)} />
        <Popconfirm title={"Wirklich auf alle markierten Anlagen anwenden?: " + rowSelection.map(val => val.key)} onConfirm={() => handleSaveAll(checkedList)}>
            <Button type="danger"  style={{float: 'right'}}>Anwenden</Button>
        </Popconfirm>
        </Card>        
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
          onClick={handleSaveAll}
          type="primary"
          style={{
              margin: "5px 5px 15px 5px",
              float: 'left'
          }} // TODO: use StyledComponend
         >Auswahl bestätigen</Button>
    </div>
  );
}

export default ConfigurationModule;
