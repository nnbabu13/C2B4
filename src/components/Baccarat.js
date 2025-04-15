import React, { useState } from 'react';
import '../styles/Baccarat.css';

function Baccarat() {
  const [cardList, setCardList] = useState([]);
  const [attempt, setAttempt] = useState(0);
  const [signal, setSignal] = useState('');

  const handlePlayerClick = () => {
    setCardList([...cardList, 'PLAYER']);
    checkResult([...cardList, 'PLAYER']);
  };

  const handleBankerClick = () => {
    setCardList([...cardList, 'BANKER']);
    checkResult([...cardList, 'BANKER']);
  };

  const checkResult = (cards) => {
    if (cards.length === 2) {
      if (cards[0] === 'PLAYER' && cards[1] === 'PLAYER') {
        setAttempt(prev => prev + 1);
        setSignal('PLAYER');
      }
      // Add more conditions here
    }
  };

  return (
    <div className="baccarat-container">
      <div className={`signal-box ${attempt >= 3 ? 'warning' : ''}`}>
        <span>{signal}</span>
      </div>
      <div className="attempt-counter">
        Attempt: {attempt}
      </div>
      <div className="controls">
        <button onClick={handlePlayerClick}>Player</button>
        <button onClick={handleBankerClick}>Banker</button>
      </div>
    </div>
  );
}

export default Baccarat;