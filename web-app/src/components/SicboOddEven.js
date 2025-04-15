import React, { useState } from 'react';
import '../styles/SicboOddEven.css';

function SicboOddEven({ onBack }) {
  const [diceResults, setDiceResults] = useState([]);
  const [attempt, setAttempt] = useState(0);
  const [signal, setSignal] = useState('');
  const [signalColor, setSignalColor] = useState('normal');
  const [strategy, setStrategy] = useState('moneyPicker'); // moneyPicker, matrix256, c2bet4

  const handleOddClick = () => {
    const newResult = { type: 'ODD', value: Math.floor(Math.random() * 6) + 1 };
    setDiceResults([...diceResults, newResult]);
    checkResult([...diceResults, newResult]);
  };

  const handleEvenClick = () => {
    const newResult = { type: 'EVEN', value: Math.floor(Math.random() * 6) + 1 };
    setDiceResults([...diceResults, newResult]);
    checkResult([...diceResults, newResult]);
  };

  const checkResult = (results) => {
    if (results.length >= 2) {
      const lastTwo = results.slice(-2);
      
      if (lastTwo[0].type === lastTwo[1].type) {
        setAttempt(prev => prev + 1);
        setSignal(`BET ON ${lastTwo[0].type === 'ODD' ? 'EVEN' : 'ODD'}`);
        updateSignalColor(attempt + 1);
      } else {
        setAttempt(0);
        setSignal('WAIT');
        setSignalColor('normal');
      }
    }
  };

  const updateSignalColor = (attemptCount) => {
    if (attemptCount >= 4) {
      setSignalColor('critical');
    } else if (attemptCount >= 3) {
      setSignalColor('warning');
    } else {
      setSignalColor('normal');
    }
  };

  return (
    <div className="sicbo-container">
      <button className="back-button" onClick={onBack}>Back</button>
      <div className="strategy-selector">
        <button 
          className={`strategy-button ${strategy === 'moneyPicker' ? 'active' : ''}`}
          onClick={() => setStrategy('moneyPicker')}>
          Money Picker
        </button>
        <button 
          className={`strategy-button ${strategy === 'matrix256' ? 'active' : ''}`}
          onClick={() => setStrategy('matrix256')}>
          Matrix 256
        </button>
        <button 
          className={`strategy-button ${strategy === 'c2bet4' ? 'active' : ''}`}
          onClick={() => setStrategy('c2bet4')}>
          C2 Bet4
        </button>
      </div>
      <div className={`signal-box ${signalColor}`}>
        <span>{signal}</span>
      </div>
      <div className={`attempt-counter ${signalColor}`}>
        Attempt: {attempt}
      </div>
      <div className="controls">
        <button className="control-button" onClick={handleOddClick}>Odd</button>
        <button className="control-button" onClick={handleEvenClick}>Even</button>
      </div>
      <div className="results-grid">
        {diceResults.map((result, index) => (
          <div key={index} className={`result-item ${result.type.toLowerCase()}`}>
            {result.type} ({result.value})
          </div>
        ))}
      </div>
    </div>
  );
}

export default SicboOddEven;