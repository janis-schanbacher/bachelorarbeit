import React from 'react';
// import { Counter } from './components/counter/Counter';
import AnalysisModule from './components/analysisModule/AnalysisModule'
import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import ConfigurationModule from './components/configurationModule/ConfigurationModule';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        {/* <Counter /> */}
      </header>
      <nav>
          <ul>
            <li>
              <Link to="/">Auswertung</Link>
            </li>
            <li>
              <Link to="/config">Konfiguration</Link>
            </li>
            {/* <li>
              <Link to="/users">Users</Link>
            </li> */}
          </ul>
        </nav>

        <Switch>
                <Route path="/" component={AnalysisModule} exact />
                <Route path="/config" component={ConfigurationModule} />
                {/* <Route path="/shop" component={Shop} /> */}
                <Route component={Error} />
        </Switch>
    </div>
  );
}

export default App;
