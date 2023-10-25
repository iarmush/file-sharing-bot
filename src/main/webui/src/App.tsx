import React from 'react';
import logo from './logo.svg';
import './App.css';
import '../node_modules/bootstrap/dist/css/bootstrap.css';

function App() {
  const queryParams = new URLSearchParams(window.location.search);
  const chatId = queryParams.get('chatId');

  function download() {
    window.location.href = `http://127.0.0.1:8091/api/v1/file-sharing-bot/download?chatId=${chatId}`;
  }

  return (
      <div className="App">
        <h1 className="display-1"> Click on the button to download</h1>
        <button className="btn btn-primary" onClick={() => download()}>Download</button>
      </div>
  );
}

export default App;
