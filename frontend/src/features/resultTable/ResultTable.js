import React, { useContext, useState, useEffect, useRef } from 'react';
import { Table, Input, Button, Popconfirm, Form } from 'antd';
import axios from "axios";
import qs from 'qs'
import { apiUrl } from "../../helper/url";
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

const ResultTable = () => {
  const [dataSource, setDataSource] = useState([
    {
      key: '0',
      code: 'ACO.001',
      textFragments: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.',
      address: 'London, Park Lane no. 0',
    },
    {
      key: '1',
      code: 'ACO.002',
      textFragments: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.',
      address: 'London, Park Lane no. 1',
    },
  ]);

  const [count, setCount] = useState(2);

  useEffect(() => {
    axios.get(`${apiUrl}/analyse`, {
      params: {
        codes: ["ACO.001", "ACO.002"] // TODO: pass from Codes seletion
      },
      paramsSerializer: params => {
        return qs.stringify(params)
      }
    })
    .then((res) => {

      const { data } = res;
  //     // fillDataSource(Object.keys(data).map(key => data[key]));
      console.log(data) 
  }).catch((err) => {
    console.log(err)
  });
},  [dataSource]);

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
            value={val}
            autosize={{ minRows: 2, maxRows: 6 }}
            defaultValue={""}
          />
        );},
    },
    {
      title: 'address',
      dataIndex: 'address',
    },
    {
      title: 'Bestätigen',
      dataIndex: 'confirm',
      render: (_, record) =>
        dataSource.length >= 1 ? (
          // <Popconfirm title="Sure to save?" onConfirm={() => handleSave(record.key)}>
          //   <a>Speichern</a>
          // </Popconfirm>
          <Button type="primary">Speichern</Button>
        ) : null,
    },
  ];

  const handleDelete = (key) => {
  // const dataSource = [...this.state.dataSource];

    setDataSource(dataSource.filter((item) => item.key !== key));
  };

  const handleAdd = () => {
    const newData = {
      key: count,
      name: `Edward King ${count}`,
      age: '32',
      address: `London, Park Lane no. ${count}`,
    };
    setDataSource([...dataSource, newData]);
    setCount(count + 1);
  };

  const handleSave = (row) => {
    const newData = dataSource;
    const index = newData.findIndex((item) => row.key === item.key);
    const item = newData[index];
    newData.splice(index, 1, { ...item, ...row });
    setDataSource(newData);
  };

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

  const rowSelection = {
    onChange: (selectedRowKeys, selectedRows) => {
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
        <Button
          onClick={handleAdd}
          type="primary"
          style={{
              marginBottom: 16,
          }}
        >
        Add a row
        </Button>
        <Table
         rowSelection={{
          type: "checkbox",
          ...rowSelection,
        }}
          components={components}
          rowClassName={() => 'editable-row'}
          bordered
          dataSource={dataSource}
          columns={columnsRender}
        />
    </div>
  );
}

export default ResultTable
