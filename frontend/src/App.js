import React from 'react';
// import { Counter } from './components/counter/Counter';
import AnalysisModule from './components/analysisModule/AnalysisModule'
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        {/* <Counter /> */}
        <h1>Analyse Auswertung</h1>
      </header>
        <AnalysisModule />
    </div>
  );
}

export default App;
