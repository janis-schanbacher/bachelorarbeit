import React, { useState, useReducer } from "react";
import { Typography, Table, Button, Tooltip, Checkbox, Divider, message } from "antd";
import axios from "axios";
import qs from "qs";
import find from "lodash/find";

import { apiUrl, portAnalysisService } from "../../helper/url";
import CodeSelection from "../codeSelection/CodeSelection";
import { StyledButton, StyledCheckboxGroup, StyledTitle } from "./ConfigurationModule.styles";

const { Title } = Typography;

const plainOptions = ["Anlagengröße", "Nutzungsgrad", "Temperaturdifferenz", "Rücklauftemperatur"];
const defaultCheckedList = ["Anlagengröße", "Nutzungsgrad", "Temperaturdifferenz", "Rücklauftemperatur"];

const ConfigurationModule = () => {
  const [dataSource, setDataSource] = useState([]);
  const [loading, setLoading] = useState(false);
  const [value, setValue] = useState([]);
  const [rowSelection, setRowSelection] = useState([]);
  const [treeData, setTreeData] = useState([]);
  // eslint-disable-next-line no-unused-vars
  const [__, forceUpdate] = useReducer(x => x + 1, 0);
  const [bulkSelection, setBulkSelection] = useState(defaultCheckedList);

  const handleChange = (code, list) => {
    const index = dataSource.findIndex(item => item.key === code);
    const newDataSource = dataSource;
    newDataSource[index].checkedList = list;
    setDataSource(newDataSource);
    forceUpdate();
  };

  const handleChangeBulkConfiguration = (list) => {
    setBulkSelection(list);
  };

  /**
   * Apply configuration to local configs the the selected rows.
   */
  const handleApplyBulkConfiguration = () => {
    const newDataSource = dataSource;
    const selectedKeys = rowSelection.map(row => row.key);

    for (let i = 0; i < newDataSource.length; i++) {
      if (selectedKeys.includes(newDataSource[i].key)) {
        newDataSource[i].checkedList = bulkSelection;
      }
    }
    setDataSource(newDataSource);
    forceUpdate();
  };

  const handleConfirm = (record) => {
    axios.post(`${apiUrl}:${portAnalysisService}/configs`, {
      id: record.key,
      facilitySize: record.checkedList.includes("Anlagengröße"),
      utilizationRate: record.checkedList.includes("Nutzungsgrad"),
      deltaTemperature: record.checkedList.includes("Temperaturdifferenz"),
      returnTemperature: record.checkedList.includes("Rücklauftemperatur"),
    }).then(() => {
      message.success(`Konfiguration gespeichert für Anlage: ${record.key.toUpperCase()}`);
    }).catch((err) => {
      message.error(`Fehler beim Speichern der Konfiguration der Anlage: ${record.key.toUpperCase()}: ${err.message}`);
    });
  };

  const handleConfirmSelection = () => {
    for (const record of rowSelection) {
      handleConfirm(record);
    }
  };

  const columns = [
    { title: "Anlage",
      dataIndex: "code" },
    {
      title: "Aktive Analysen",
      dataIndex: "activeAnalyses",
      render: (val, row) => {
        const index = dataSource.findIndex(item => item.key === row.key);
        return (
          <Checkbox.Group
            options={plainOptions}
            value={dataSource[index].checkedList}
            onChange={list => handleChange(row.key, list)}
          />
        );
      },
    },
    {
      title: "Bestätigen",
      dataIndex: "confirm",
      width: 40,
      render: (_, record) => (dataSource.length >= 1 ? (
        <Tooltip placement="bottom" color="black" title="Konfiguration speichern">
          <Button type="primary" onClick={e => handleConfirm(record)}>Bestätigen</Button>
        </Tooltip>
      ) : null),
    },
  ];

  const loadConfigs = () => {
    setLoading(true);
    const codes = value.filter(v => v.value.length > 3).map(v => v.label);
    // Add codes of children from selected prefix (f.i. {value: "0-1", value: "TST"})
    const keysOfPrefixes = value.filter(v => v.value.length === 3);
    for (let i = 0; i < keysOfPrefixes.length; i++) {
      const treeElement = find(treeData, ["value", keysOfPrefixes[i].value]);
      if (treeElement !== undefined && treeElement.children !== undefined) {
        for (let k = 0; k < treeElement.children.length; k++) {
          codes.push(treeElement.children[k].title);
        }
      }
    }

    // retrieve configs from backend
    axios.get(`${apiUrl}:${portAnalysisService}/configs/get-list`, {
      params: {
        codes,
      },
      paramsSerializer: params => qs.stringify(params, { arrayFormat: "repeat" }),
    })
      .then((res) => {
        const { data } = res;
        const configs = Object.keys(data).map((key) => {
          const activeAnalyses = [];
          if (data[key].facilitySize) {
            activeAnalyses.push("Anlagengröße");
          }
          if (data[key].utilizationRate) {
            activeAnalyses.push("Nutzungsgrad");
          }
          if (data[key].deltaTemperature) {
            activeAnalyses.push("Temperaturdifferenz");
          }
          if (data[key].returnTemperature) {
            activeAnalyses.push("Rücklauftemperatur");
          }
          return {
            key: data[key].id,
            code: data[key].id.toUpperCase(),
            checkedList: activeAnalyses,
          };
        });

        // add default config, if non exists yet
        for (let i = 0; i < codes.length; i++) {
          const index = configs.findIndex(c => c.code === codes[i].toUpperCase());
          if (index < 0) {
            configs.push({ key: codes[i], code: codes[i], checkedList: defaultCheckedList });
          }
        }

        setDataSource(configs);
        setLoading(false);
      }).catch((err) => {
        message.error(`Fehler beim Laden der Konfigurationen: ${err.message}`);
      });
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
      <Title level={2}>Konfiguration</Title>
      <StyledTitle level={4}>Auswahl zu konfigurierender Anlagen</StyledTitle>
      <CodeSelection value={value} setValue={setValue} treeData={treeData} setTreeData={setTreeData} />
      <Tooltip placement="bottom" color="black" title="Konfigurationen der ausgewählten Anlagen laden.">
        <StyledButton
          onClick={loadConfigs}
          type="primary"
        >
          Konfigurationen laden
        </StyledButton>
      </Tooltip>
      <Divider />
      <StyledTitle level={4}>Markierte Anlagen Konfigurieren</StyledTitle>
      <StyledCheckboxGroup
        options={plainOptions}
        defaultValue={defaultCheckedList}
        onChange={handleChangeBulkConfiguration}
      />
      <Tooltip
        placement="bottom"
        color="black"
        title="Lokal die Konfigurationen der in der Tabelle markierten Anlagen anpassen.
           Gespeichert können diese über die entsprechenden Bestätigungs-Buttons werden."
      >
        <StyledButton
          onClick={handleApplyBulkConfiguration}
          type="primary"
        >
          Übernehmen
        </StyledButton>
      </Tooltip>
      <Divider />
      <StyledTitle level={4}>Konfigurationen</StyledTitle>
      <Tooltip
        placement="bottom"
        color="black"
        title="Konfigurationen der in der Tabelle markierten Anlagen speichern."
      >
        <StyledButton
          onClick={handleConfirmSelection}
          type="primary"
        >Auswahl bestätigen
        </StyledButton>
      </Tooltip>
      <Table
        rowSelection={{
          type: "checkbox",
          ...customRowSelection,
        }}
        rowClassName={() => "editable-row"}
        bordered
        dataSource={dataSource}
        columns={columnsRender}
        loading={loading}
      />
    </div>
  );
};

export default ConfigurationModule;
