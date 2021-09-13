/* eslint-disable jsx-a11y/no-static-element-interactions */ // TODO: remove
/* eslint-disable jsx-a11y/click-events-have-key-events */ // TODO: remove

import React, { useContext, useState, useEffect, useRef } from "react";
import PropTypes from "prop-types";
import { Divider, Table, Input, Button, Form, Typography } from "antd";
import axios from "axios";
// import qs from 'qs'

import { apiUrl } from "../../helper/url";
import CodeSelection from "../codeSelection/CodeSelection";

const { Title } = Typography;
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

EditableRow.propTypes = {
  // TODO: set required type or maybe remove prop, because its not used
  index: PropTypes.func.isRequired,
};

const EditableCell = ({
  title,
  editable,
  children,
  dataIndex,
  record,
  handleConfirm,
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

  let childNode = children;

  if (editable) {
    // TODO: fix linter errors and remove ignore for whole file (
    childNode = (
      <div
        className="editable-cell-value-wrap"
        style={{
          paddingRight: 24,
        }}
        onClick={toggleEdit}
      >
        { children }
      </div>
    );
  }

  return <td {...restProps}>{childNode}</td>;
};

EditableCell.propTypes = {
  title: PropTypes.string.isRequired,
  editable: PropTypes.bool.isRequired,
  children: PropTypes.array.isRequired,
  dataIndex: PropTypes.string.isRequired,
  record: PropTypes.object.isRequired,
  handleConfirm: PropTypes.func.isRequired,
};

const AnalysisModule = () => {
  const [dataSource, setDataSource] = useState([]);
  const [originalDataSource, setOriginalDataSource] = useState([]);
  const [value, setValue] = useState(["0-0-0", "0-0-1"]); // TODO: change default to []
  const [rowSelection, setRowSelection] = useState([]);
  const [treeData, setTreeData] = useState([]);

  const handleAnalyse = () => {
    // TODO: get codes dynamically, maybe through values, or else through params of CodeSelction
    const body = ["ACO.001", "ACO.002"];
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
        setDataSource(Object.keys(data).map(key => ({ key,
          code: key,
          textFragments: data[key].join("\n") })));
      // fillDataSource(Object.keys(data).map(key => data[key]));
      // console.log(data)
      })
      .then(() => {
        setOriginalDataSource(dataSource);
      })
      .catch((err) => {
        // eslint-disable-next-line no-console
        // TODO: display Error Alert
        console.log(err);
      });
  };

  const handleChange = (e) => {
    const code = e.target.attributes.code.nodeValue;
    const newDataSource = dataSource;
    let index = -1;
    for (let i = 0; i < dataSource.length; i += 1) {
      if (dataSource[i].code === code) {
        index = i;
        break;
      }
    }
    newDataSource[index].textFragments = e.target.value;
    setDataSource(newDataSource);
  };

  const handleConfirm = (record) => {
    console.log(`handleConfirm: ${record.key}`);
    console.log(`text new: ${record.textFragments}`); // equals value in dataSource
    let textFragmentsResult = "";
    // find/log original textFragments
    for (let i = 0; i < originalDataSource.length; i += 1) {
      if (originalDataSource[i].code === record.key) {
        textFragmentsResult = originalDataSource[i].textFragments;
        // TODO: log
        console.log(`textFragmentsResult: ${textFragmentsResult}`);
        break;
      }
    }

    // TODO: test andd write controller
    axios.post(`${apiUrl}/text-fragments`, {
      code: record.key,
      textFragments: record.textFragments,
      textFragmentsResult,
    })
    // TODO: render SuccessNotification
      .then(res => console.log(`Response status for post /text-fragments of code: ${record.key}: ${res.status}`));
  };

  // TODO: weiter hier
  const handleConfirmSelection = () => {
    console.log(rowSelection);
    rowSelection.forEach(row => handleConfirm(row));
    // TODO: render SuccessNotification.
  };

  // const handleDelete = (key) => {
  //   setDataSource(dataSource.filter((item) => item.key !== key));
  // };

  const columns = [
    { title: "Anlagencode",
      dataIndex: "code" },
    {
      title: "Textbausteine",
      dataIndex: "textFragments",
      width: "60%",
      // editable: true,
      render: (val, row) => (
        <Input.TextArea
          // value={val}
          autosize={{ minRows: 2, maxRows: 6 }}
          defaultValue={val}
          onChange={handleChange}
          code={row.key}
        />
      ),
    },
    {
      title: "Bestätigen",
      dataIndex: "confirm",
      render: (_, record) => (dataSource.length >= 1 ? (
      // <Popconfirm title="Sure to save?" onConfirm={() => handleConfirm(record.key)}>
      //   <a>Speichern</a>
      // </Popconfirm>
        <Button type="primary" onClick={e => handleConfirm(record)}>Speichern</Button>
      ) : null),
    },
  ];

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
      onCell: record => ({
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
    },
    getCheckboxProps: record => ({
      disabled: record.name === "Disabled User",
      // Column configuration not to be checked
      name: record.name,
    }),
  };

  // TODO: move CodeSelection and Analysis Button to parent
  return (
    <div>
      <Title level={2}>Analyse Oberfläche</Title>
      <Title level={4} style={{ textAlign: "left" }}>Auswahl zu analysierender Anlagen</Title>

      <CodeSelection value={value} setValue={setValue} treeData={treeData} setTreeData={setTreeData} />
      <Button
        onClick={handleAnalyse}
        type="primary"
        style={{
          margin: "5px 5px 15px 5px",
          float: "left",
        }} // TODO: use StyledComponend
      >
        Analysieren
      </Button>
      <Divider />
      <Title level={4} style={{ textAlign: "left" }}>Analyse-Ergebnisse</Title>
      <Table
        rowSelection={{
          type: "checkbox",
          ...customRowSelection,
        }}
        components={components}
        rowClassName={() => "editable-row"}
        bordered
        dataSource={dataSource}
        columns={columnsRender}
      />
      <Button
        onClick={handleConfirmSelection}
        type="primary"
        style={{
          margin: "5px 5px 15px 5px",
          float: "left",
        }}
      >Auswahl bestätigen
      </Button>
    </div>
  );
};

export default AnalysisModule;
