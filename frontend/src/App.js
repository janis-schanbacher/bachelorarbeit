import React from "react";
import "./App.css";
import {
  Switch,
  Route,
  Link,
  useLocation,
} from "react-router-dom";
import { Layout } from "antd";
import Navbar from "./components/navbar/Navbar";

import AnalysisModule from "./components/analysisModule/AnalysisModule";
import ConfigurationModule from "./components/configurationModule/ConfigurationModule";

const { Header, Content } = Layout;

const App = () => {
  const location = useLocation();

  return (
    <div className="App">
      <Header style={{ background: "white", borderBottom: "1px solid #f0f0f0" }}>
        <Link to="" style={{ float: "left" }}><img src="/ewus_logo.png" alt="Logo EWUS Berlin" width="100" /></Link>
        <Navbar currentUrl={location.pathname} />
      </Header>
      <Content style={{ padding: "0 50px" }}>
        <Switch>
          <Route path="/" component={AnalysisModule} exact />
          <Route path="/config" component={ConfigurationModule} />
          <Route component={Error} />
        </Switch>
      </Content>
    </div>
  );
};

export default App;
