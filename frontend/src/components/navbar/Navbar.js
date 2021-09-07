import React, { useState } from 'react';
import { Menu } from 'antd';
import { EditOutlined, SettingOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom/cjs/react-router-dom.min';


const Navbar = () => {
    // TODO: effect, when url changed update active item. 
    const [current, setCurrent] = useState('');

    const handleClick = e => {
        console.log('click ', e);
        setCurrent(e.key);
    };
    return (
        <Menu onClick={handleClick} selectedKeys={[current]} mode="horizontal" style={{float: "right"}} >
            <Menu.Item key="analyse" icon={<EditOutlined />}>
                <Link to="/">Analyse</Link>
            </Menu.Item>
            <Menu.Item key="config" icon={<SettingOutlined />}>
                <Link to="/config">Konfiguration</Link>
            </Menu.Item>
      </Menu>
    );
  }

export default Navbar;