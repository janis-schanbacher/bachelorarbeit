import React from 'react';
// import { Counter } from './components/counter/Counter';
import ResultTable from './components/resultTable/ResultTable'
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        {/* <Counter /> */}
        <h1>Analyse Auswertung</h1>
      </header>
        <ResultTable />
    </div>
  );
}

export default App;
