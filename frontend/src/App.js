import React from "react";
// import { Counter } from './components/counter/Counter';
import "./App.css";
import {
  Switch,
  Route,
  Link,
} from "react-router-dom";
import { Layout } from "antd";
import Navbar from "./components/navbar/Navbar";

import AnalysisModule from "./components/analysisModule/AnalysisModule";
import ConfigurationModule from "./components/configurationModule/ConfigurationModule";

const { Header, Content } = Layout;

function App() {
  return (
    <div className="App">
      <Header style={{ background: "white", borderBottom: "1px solid #f0f0f0" }}>
        <Link to="" style={{ float: "left" }}><img src="/ewus_logo.png" alt="Logo EWUS Berlin" width="100" /></Link>
        <Navbar />
      </Header>
      <Content style={{ padding: "0 50px" }}>
        <Switch>
          <Route path="/" component={AnalysisModule} exact />
          <Route path="/config" component={ConfigurationModule} />
          {/* <Route path="/shop" component={Shop} /> */}
          <Route component={Error} />
        </Switch>
      </Content>
    </div>
  );
}

export default App;
