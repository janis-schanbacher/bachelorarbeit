/* eslint-disable jsx-a11y/no-static-element-interactions */ // TODO: remove
/* eslint-disable jsx-a11y/click-events-have-key-events */ // TODO: remove

import React, { useContext, useState, useEffect, useRef } from "react";
import PropTypes from "prop-types";
import { Divider, Table, Input, Button, Tooltip, Form, Typography, message } from "antd";
import axios from "axios";
// import qs from 'qs'

import { apiUrl, portAnalysisService, portEnergielenkerEneffcoService } from "../../helper/url";
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
  const [value, setValue] = useState([]); // TODO: change default to []
  const [rowSelection, setRowSelection] = useState([]);
  const [treeData, setTreeData] = useState([]);
  const [loading, setLoading] = useState(false);

  const getSelectedCodes = () => {
    const selectedCodes = value.map((v) => {
      const { label } = v;
      if (label.length < 7) {
        return treeData.find(node => node.title.includes(`${label}`)).children.map(n => n.title);
      }
      return label;
    }).flat();
    return selectedCodes;
  };

  const handleAnalyse = () => {
    setLoading(true);
    const body = getSelectedCodes();
    axios.post(`${apiUrl}:${portAnalysisService}/analyse`, body)

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
        setDataSource(Object.keys(data).map((key) => {
          const row = {
            key,
            code: key,
            textFragments: data[key].filter(tf => !tf.includes("prev: ")).join("\n"),
            textFragmentsPrev: data[key].find(tf => tf.includes("prev: ")).substring(6),
          };
          if (row.textFragmentsPrev === "null") {
            row.textFragmentsPrev = "";
          }
          return row;
        }).sort((a, b) => ((a.key < b.key) ? -1 : 1)));
      // fillDataSource(Object.keys(data).map(key => data[key]));
      // console.log(data)
      // TODO: if data.size < codes.size indicate error for the missing ones.
      //  With hint, probably Energielenker fields missing
      })
      .then(() => {
        setOriginalDataSource(dataSource);
        setLoading(false);
      })
      .catch((err) => {
        // eslint-disable-next-line no-console
        message.error(`Fehler beim Laden der Analyseergebnisse: ${err.message}`);
        setLoading(false);
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
    let textFragmentsAnalysisResult = "";
    // find/log original textFragments
    for (let i = 0; i < originalDataSource.length; i += 1) {
      if (originalDataSource[i].code === record.key) {
        textFragmentsAnalysisResult = originalDataSource[i].textFragments;
        break;
      }
    }

    axios.post(`${apiUrl}:${portEnergielenkerEneffcoService}/text-fragments`, {
      id: record.key,
      textFragments: record.textFragments,
      textFragmentsAnalysisResult,
    })
      // TODO: render SuccessNotification
      .then((res) => {
        console.log(`Response status for post /text-fragments of id: ${record.key}: ${res.status}`);
        message.success(`Textbaustetine gespeichert für Anlage: ${record.key}`);
      }).catch((err) => {
        message.error(`Fehler beim Speichern der Textbausteine für Anlage: ${record.key}: ${err.message}`);
      });
  };

  const handleConfirmSelection = () => {
    console.log(rowSelection);
    rowSelection.forEach((row) => {
      handleConfirm(row);
    });
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
      width: "45%",
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
      title: "Vorige Textbausteine",
      dataIndex: "textFragmentsPrev",
      key: "textFragmentsPrev",
      width: "25%",
    },
    {
      title: "Bestätigen",
      dataIndex: "confirm",
      render: (_, record) => (dataSource.length >= 1 ? (
      // <Popconfirm title="Sure to save?" onConfirm={() => handleConfirm(record.key)}>
      //   <a>Speichern</a>
      // </Popconfirm>
        <Tooltip placement="bottom" color="black" title="Textbausteine in Energielenker speichern.">
          <Button type="primary" onClick={e => handleConfirm(record)}>Bestätigen</Button>
        </Tooltip>
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
      <Tooltip
        placement="bottom"
        color="black"
        title="Ausgewählte Anlagen entsprechend der zugehörigen Konfigurationen analysieren."
      >
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
      </Tooltip>
      <Divider />
      <Title level={4} style={{ textAlign: "left" }}>Analyse-Ergebnisse</Title>
      <Tooltip
        placement="bottom"
        color="black"
        title="Textbausteine der in der Tabelle markierten Anlagen in Energielenker speichern."
      >
        <Button
          onClick={handleConfirmSelection}
          type="primary"
          style={{
            margin: "5px 5px 15px 5px",
            float: "left",
          }}

        >Auswahl bestätigen
        </Button>
      </Tooltip>
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
        loading={loading}
      />
    </div>
  );
};

export default AnalysisModule;
