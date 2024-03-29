/* eslint-disable jsx-a11y/no-static-element-interactions */
/* eslint-disable jsx-a11y/click-events-have-key-events */
import React, { useContext, useState, useEffect, useRef } from "react";
import PropTypes from "prop-types";
import { Divider, Table, Input, Button, Tooltip, Form, Typography, message } from "antd";
import axios from "axios";
import noop from "lodash/noop";

import { apiUrl, portAnalysisService, portEnergielenkerEneffcoService } from "../../helper/url";
import CodeSelection from "../codeSelection/CodeSelection";
import { StyledButton, StyledTitle } from "./AnalysisModule.styles";

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
  index: PropTypes.number,
};

EditableRow.defaultProps = {
  index: 0,
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
    childNode = (
      <div onClick={toggleEdit}>
        { children }
      </div>
    );
  }

  return <td {...restProps}>{childNode}</td>;
};

EditableCell.propTypes = {
  title: PropTypes.string,
  editable: PropTypes.bool,
  children: PropTypes.array,
  dataIndex: PropTypes.string,
  record: PropTypes.object,
  handleConfirm: PropTypes.func,
};

EditableCell.defaultProps = {
  title: "",
  editable: false,
  children: [],
  dataIndex: "",
  record: null,
  handleConfirm: noop,
};

const AnalysisModule = () => {
  const [dataSource, setDataSource] = useState([]);
  const [originalDataSource, setOriginalDataSource] = useState([]);
  const [value, setValue] = useState([]);
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
      })
      .then(() => {
        setOriginalDataSource(dataSource);
        setLoading(false);
      })
      .catch((err) => {
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
      .then((res) => {
        message.success(`Textbausteine gespeichert für Anlage: ${record.key}`);
      }).catch((err) => {
        message.error(`Fehler beim Speichern der Textbausteine für Anlage: ${record.key}: ${err.message}`);
      });
  };

  const handleConfirmSelection = () => {
    rowSelection.forEach((row) => {
      handleConfirm(row);
    });
  };

  const columns = [
    { title: "Anlage",
      dataIndex: "code",
      width: 35 },
    {
      title: "Textbausteine",
      dataIndex: "textFragments",
      width: "45%",
      render: (val, row) => (
        <Input.TextArea
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
      // width: "25%",

    },
    {
      title: "Bestätigen",
      dataIndex: "confirm",
      width: 40,
      render: (_, record) => (dataSource.length >= 1 ? (
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

  return (
    <div>
      <Title level={2}>Analyse</Title>
      <StyledTitle level={4}>Auswahl zu analysierender Anlagen</StyledTitle>
      <CodeSelection value={value} setValue={setValue} treeData={treeData} setTreeData={setTreeData} />
      <Tooltip
        placement="bottom"
        color="black"
        title="Ausgewählte Anlagen entsprechend der zugehörigen Konfigurationen analysieren."
      >
        <StyledButton
          onClick={handleAnalyse}
          type="primary"
        >
          Analysieren
        </StyledButton>
      </Tooltip>
      <Divider />
      <StyledTitle level={4}>Analyse-Ergebnisse</StyledTitle>
      <Tooltip
        placement="bottom"
        color="black"
        title="Textbausteine der in der Tabelle markierten Anlagen in Energielenker speichern."
      >
        <StyledButton
          onClick={handleConfirmSelection}
          type="primary"
        >
          Auswahl bestätigen
        </StyledButton>
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
