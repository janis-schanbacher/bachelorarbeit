import React from "react";
import "./App.css";
import {
  Switch,
  Route,
  useLocation,
} from "react-router-dom";

import Navbar from "./components/navbar/Navbar";
import AnalysisModule from "./components/analysisModule/AnalysisModule";
import ConfigurationModule from "./components/configurationModule/ConfigurationModule";
import { AppWrapper, StyledContent, StyledHeader, StyledLink } from "./App.styles";

const App = () => {
  const location = useLocation();

  return (
    <AppWrapper>
      <StyledHeader>
        <StyledLink to=""><img src="/ewus_logo.png" alt="Logo EWUS Berlin" width="100" /></StyledLink>
        <Navbar currentUrl={location.pathname} />
      </StyledHeader>
      <StyledContent>
        <Switch>
          <Route path="/" component={AnalysisModule} exact />
          <Route path="/config" component={ConfigurationModule} />
          <Route component={Error} />
        </Switch>
      </StyledContent>
    </AppWrapper>
  );
};

export default App;
