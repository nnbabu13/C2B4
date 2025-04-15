import React, { useState } from 'react';
import Baccarat from './components/Baccarat';
import SicboOddEven from './components/SicboOddEven';
import './App.css';

function App() {
  const [currentGame, setCurrentGame] = useState(null);

  return (
    <div className="App">
      <header className="App-header">
        <h1>Game Companion</h1>
      </header>
      <main>
        {!currentGame && (
          <div className="game-selection">
            <button className="game-button" onClick={() => setCurrentGame('baccarat')}>Baccarat</button>
            <button className="game-button" onClick={() => setCurrentGame('sicbo')}>Sic Bo Odd/Even</button>
          </div>
        )}
        {currentGame === 'baccarat' && <Baccarat onBack={() => setCurrentGame(null)} />}
        {currentGame === 'sicbo' && <SicboOddEven onBack={() => setCurrentGame(null)} />}
      </main>
    </div>
  );
}

export default App;