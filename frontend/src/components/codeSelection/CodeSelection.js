import React, { useEffect } from "react";
import { TreeSelect } from "antd";
import axios from "axios";
import PropTypes from "prop-types";

import { apiUrl, portEnergielenkerEneffcoService } from "../../helper/url";

const { SHOW_PARENT } = TreeSelect;

const CodeSelection = ({ value, setValue, treeData, setTreeData }) => {
  // const [value, setValue] = useState([]);
  // const [treeData, setTreeData] = useState([]);

  const createTreeData = (codes) => {
    // Collect prefixes
    const prefixes = [];
    for (const code of codes) {
      const prefix = code.split(".")[0];
      if (prefixes.indexOf(prefix) <= -1) { // prefix is not present in prefixes
        prefixes.push(prefix);
      }
    }
    prefixes.sort();

    // create 1st level of treeData using the prefixes
    const treeDataBuilt = Object.keys(prefixes).map(key => ({
      title: prefixes[key],
      value: `0-${key}`,
      key: `0-${key}`,
      children: [],
    }));

    // create 2nd level of treeData by adding codes to the prefixes
    for (const code of codes) {
      for (const element of treeDataBuilt) {
        if (code.includes(element.title)) {
          element.children.push({
            title: code,
            value: `${element.value}-${element.children.length}`,
            key: `${element.key}-${element.children.length}`,
          });
          break;
        }
      }
    }

    return treeDataBuilt;
  };

  useEffect(() => {
    axios.get(`${apiUrl}:${portEnergielenkerEneffcoService}/facility-codes`)
      .then((res) => {
        const { data } = res;
        setTreeData(createTreeData(data));
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const onChange = (val) => {
    setValue(val);
  };

  const tProps = {
    treeData,
    value,
    labelInValue: true,
    onChange,
    treeCheckable: true,
    showCheckedStrategy: SHOW_PARENT,
    placeholder: "Bitte Anlagen auswählen",
    style: {
      width: "100%",
    },
    allowClear: true,
  };
  return <TreeSelect {...tProps} />;
};

CodeSelection.propTypes = {
  value: PropTypes.array.isRequired,
  setValue: PropTypes.func.isRequired,
  treeData: PropTypes.array.isRequired,
  setTreeData: PropTypes.func.isRequired,
};

export default CodeSelection;
