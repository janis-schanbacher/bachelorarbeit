import React, { useState, useEffect} from 'react';
import { TreeSelect } from 'antd';
import axios from "axios";
import { apiUrl } from "../../helper/url";

const { SHOW_PARENT } = TreeSelect;

// TODO: Replace with mapping from codes from EL/DB
// const treeData = [
//   {
//     title: 'ACO',
//     value: '0-0',
//     key: '0-0',
//     children: [
//       {
//         title: 'ACO.001',
//         value: '0-0-0',
//         key: '0-0-0',
//       },
//       {
//         title: 'ACO.002',
//         value: '0-0-1',
//         key: '0-0-1',
//       },
//     ],
//   },
//   {
//     title: 'Node2',
//     value: '0-1',
//     key: '0-1',
//     children: [
//       {
//         title: 'Child Node3',
//         value: '0-1-0',
//         key: '0-1-0',
//       },
//       {
//         title: 'Child Node4',
//         value: '0-1-1',
//         key: '0-1-1',
//       },
//       {
//         title: 'Child Node5',
//         value: '0-1-2',
//         key: '0-1-2',
//       },
//     ],
//   },
// ];

// TODO: AnalysisPage > [CodeSelection, AnalyseButton, ResultTable]
const CodeSelection = () => { 
    const [value, setValue] = useState(['0-0-0', '0-0-1']); // TODO: change default to []
    const [treeData, setTreeData] = useState([]);  
    useEffect(() => {
        axios.get(`${apiUrl}/facility-codes`)
        .then((res) => {
            const { data } = res;
            setTreeData(createTreeData(data));
        });
    },  []);

    const createTreeData = (codes) => {
        // Collect prefixes
        const prefixes = [];
        for (const code of codes) {
            const prefix = code.split(".")[0];
            if (prefixes.indexOf(prefix) <= -1) {// prefix is not present in prefixes 
                prefixes.push(prefix);
            }
        }
        prefixes.sort();
        console.log(prefixes);

        // create 1st level of treeData using the prefixes
        const treeData = Object.keys(prefixes).map(key => {    
            return {
                    title: prefixes[key],
                    value: '0-' + key,
                    key: '0-' + key,
                    children: []
            }
        })

        // TODO: add levels for xxx.x, xxx.xx
        // create 2nd level of treeData by adding codes to the prefixes
        for (const code of codes) {
            for (const element of treeData){
                if (code.includes(element.title)){
                    element.children.push({
                        title: code,
                        value: element.value + "-" + element.children.length,
                        key: element.key + "-" + element.children.length
                    })
                    break;
                }
            }
        }

        console.log(treeData);
        return treeData;
    }

    const onChange = value => {
        console.log('onChange ', value);
        setValue(value);
    };

    const tProps = {
      treeData,
      value: value,
    //   defaultValue: ["ACO.001", "ACO.002"],
      onChange: onChange,
      treeCheckable: true,
      showCheckedStrategy: SHOW_PARENT,
      placeholder: 'Please select',
      style: {
        width: '100%',
      },
      allowClear: true,
    };
    return <TreeSelect {...tProps} />;
}

export default CodeSelection;