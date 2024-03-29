import React, { useState, useEffect } from "react";
import { EditOutlined, SettingOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom/cjs/react-router-dom.min";
import PropTypes from "prop-types";
import { Menu } from "antd";

import { StyledMenu } from "./Navbar.styles";

const Navbar = ({ currentUrl }) => {
  const [current, setCurrent] = useState(currentUrl);
  // Update highlighted Menu Item, when Url is changed by clicking the logo or changing the URL manually
  useEffect(() => {
    if (currentUrl === "/") {
      setCurrent("analyse");
    } else if (currentUrl === "/config") {
      setCurrent("config");
    }
  }, [currentUrl]);

  const handleClick = (e) => {
    setCurrent(e.key);
  };
  return (
    <StyledMenu onClick={handleClick} selectedKeys={[current]} mode="horizontal">
      <Menu.Item key="analyse" icon={<EditOutlined />}>
        <Link to="/">Analyse</Link>
      </Menu.Item>
      <Menu.Item key="config" icon={<SettingOutlined />}>
        <Link to="/config">Konfiguration</Link>
      </Menu.Item>
    </StyledMenu>
  );
};

Navbar.propTypes = {
  currentUrl: PropTypes.string,
};

Navbar.defaultProps = {
  currentUrl: "/",
};

export default Navbar;
