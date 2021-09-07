import React, { useContext, useState, useEffect, useRef } from 'react';
import { Table, Input, Button, Form } from 'antd';
import axios from "axios";
// import qs from 'qs'
import { apiUrl } from "../../helper/url";
import CodeSelection from '../codeSelection/CodeSelection';

const EditableContext = React.createContext(null);

const EditableRow = ({ index, ...props }) => {
  const [form] = Form.useForm();
  return (
    <Form form={form} component={false}>
      <EditableContext.Provider value={form}>
        <tr {...props} />
      </EditableContext.Provider>
    </Form>
  );
};

const EditableCell = ({
    title,
    editable,
    children,
    dataIndex,
    record,
    handleSave,
    ...restProps
}) => {
  const [editing, setEditing] = useState(false);
  const inputRef = useRef(null);
  const form = useContext(EditableContext);
  useEffect(() => {
    if (editing) {
      inputRef.current.focus();
    }
  }, [editing]);

  const toggleEdit = () => {
    setEditing(!editing);
    form.setFieldsValue({
      [dataIndex]: record[dataIndex],
    });
  };

  const save = async () => {
    try {
      const values = await form.validateFields();
      toggleEdit();
      handleSave({ ...record, ...values });
    } catch (errInfo) {
      console.log('Save failed:', errInfo);
    }
  };

  let childNode = children;

  if (editable) {
    childNode = editing ? (
      <Form.Item
        style={{
          margin: 0,
        }}
        name={dataIndex}
        rules={[
          {
            required: true,
            message: `${title} is required.`,
          },
        ]}
      >
        <Input ref={inputRef} onPressEnter={save} onBlur={save} />
      </Form.Item>
    ) : (
      <div
        className="editable-cell-value-wrap"
        style={{
          paddingRight: 24,
        }}
        onClick={toggleEdit}
      >
        {children}
      </div>
    );
  }

  return <td {...restProps}>{childNode}</td>;
};

const AnalysisModule = () => {
  const [dataSource, setDataSource] = useState([]);
  const [originalDataSource, setOriginalDataSource] = useState([]);
  const [value, setValue] = useState(['0-0-0', '0-0-1']); // TODO: change default to []
  const [rowSelection, setRowSelection] = useState([])

  const handleChange = (e) => {
    const code = e.target.attributes.code.nodeValue;
    console.log(code);
    const newDataSource = dataSource;
    var index = -1;
    for (var i = 0; i < dataSource.length; i += 1) {
      if (dataSource[i]["code"] === code) {
          index = i;
          break;
      }
    }
    console.log(e.target.value);
    newDataSource[index]["textFragments"] = e.target.value;
    setDataSource(newDataSource);
  }

  const columns = [
    { title: "Anlagencode",
      dataIndex: "code",
    },
    {
      title: 'Textbausteine',
      dataIndex: 'textFragments',
      width: '40%',
      // editable: true,
      render: (val, row) => {
        return (
          <Input.TextArea
            // value={val}
            autosize={{ minRows: 2, maxRows: 6 }}
            defaultValue={val}
            onChange={handleChange}
            code={row.key}
          />
        );},
    },
    {
      title: 'Bestätigen',
      dataIndex: 'confirm',
      render: (_, record) =>
        dataSource.length >= 1 ? (
          // <Popconfirm title="Sure to save?" onConfirm={() => handleSave(record.key)}>
          //   <a>Speichern</a>
          // </Popconfirm>
          <Button type="primary" onClick={(e) => handleSave(record)}>Speichern</Button>
        ) : null,
    },
  ];

  const handleAnalyse = () => {
    const body = value; // ["ACO.001", "ACO.002"];
    axios.post(`${apiUrl}/analyse`, body)

    // axios.get(`${apiUrl}/analyse`, {
    //   params: {
    //     codes: { ["ACO.001", "ACO.002"]} 
    //   },
    //   paramsSerializer: params => {
    //     return qs.stringify(params)
    //   }
    // })
    .then((res) => {

      const { data } = res;
      setDataSource(Object.keys(data).map(key => {
        return {
          key: key, 
          code: key, 
          textFragments: data[key].join(';\n')}
      }))
      // fillDataSource(Object.keys(data).map(key => data[key]));
      // console.log(data) 
    })
    .then(() => {
      setOriginalDataSource(dataSource);
    })
    .catch((err) => {
      console.log(err)
    });
  }

  const handleSave = (record) => {
    console.log("handleSave: " + record.key);
    console.log("text new: " + record.textFragments); // equals value in dataSource

    for (var i = 0; i < originalDataSource.length; i += 1) {
      if (originalDataSource[i]["code"] === record.key) {
        console.log("text previous: " + originalDataSource[i]["textFragments"])
        // TODO: log
        break;
      }
    }

    // TODO: request to save to EL
  };

  const handleSaveAll = () => {
    console.log(rowSelection);
    // console.log(selectedRowKeys);
    // const newData = dataSource;
    // const index = newData.findIndex((item) => row.key === item.key);
    // const item = newData[index];
    // newData.splice(index, 1, { ...item, ...row });
    // setDataSource(newData);
  };

  // const handleDelete = (key) => {
  //   setDataSource(dataSource.filter((item) => item.key !== key));
  // };

  const components = {
    body: {
      row: EditableRow,
      cell: EditableCell,
    },
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
      handleSave: handleSave,
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

  // TODO: move CodeSelection and Analysis Button to parent
  return(
    <div>
      <h1>Analyse Oberfläche</h1>
        <CodeSelection value={value} setValue={setValue} /> 
        <Button
          onClick={handleAnalyse}
          type="primary"
          style={{
              margin: "5px 5px 15px 5px",
              float: 'left'
          }} // TODO: use StyledComponend
        >
        Analysieren
        </Button>
        <Table
         rowSelection={{
          type: "checkbox",
          ...customRowSelection,
        }}
          components={components}
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

export default AnalysisModule
