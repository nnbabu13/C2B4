import React, { useState, useEffect } from 'react';
import '../styles/Baccarat.css';

function Baccarat({ onBack }) {
  const [waitStartIndex, setWaitStartIndex] = useState(-1);
  const [cardList, setCardList] = useState([]);
  const [attempt, setAttempt] = useState(0);
  const [signal, setSignal] = useState('');
  const [signalColor, setSignalColor] = useState('normal');
  const [prediction, setPrediction] = useState([]);
  const [currentPredictionIndex, setCurrentPredictionIndex] = useState(0);
  const [waitMode, setWaitMode] = useState(false);
  const [predictionType, setPredictionType] = useState('waiting');

  const getPredictionSequence = (card1, card2) => {
    if (card1 === 'BANKER' && card2 === 'BANKER') return ['BANKER', 'PLAYER', 'PLAYER', 'PLAYER'];
    if (card1 === 'PLAYER' && card2 === 'PLAYER') return ['PLAYER', 'BANKER', 'BANKER', 'BANKER'];
    if (card1 === 'BANKER' && card2 === 'PLAYER') return ['BANKER', 'BANKER', 'PLAYER', 'BANKER'];
    if (card1 === 'PLAYER' && card2 === 'BANKER') return ['PLAYER', 'PLAYER', 'BANKER', 'PLAYER'];
    return [];
  };

  const checkResult = (cards) => {
    if (waitMode) {
      setPredictionType('waiting');
      // Get only results after wait mode started
      const postWinResults = cards.slice(waitStartIndex);
      
      if (postWinResults.length === 2) {
        const newPrediction = getPredictionSequence(postWinResults[0], postWinResults[1]);
        if (newPrediction.length > 0) {
          setSignal(`NEW SEQUENCE - BET ON ${newPrediction[0]}`);
          setPrediction(newPrediction);
          setCurrentPredictionIndex(0);
          setWaitMode(false);
          setWaitStartIndex(-1);
          setAttempt(0);
        }
      } else {
        setSignal(`COLLECTING NEW RESULTS (${postWinResults.length}/2)`);
      }
      return;
    }

    // Initial pattern or continuing sequence
    if (cards.length === 2) {
      const newPrediction = getPredictionSequence(cards[0], cards[1]);
      if (newPrediction.length > 0) {
        setPredictionType(newPrediction[0].toLowerCase());
        setSignal(`BET ON ${newPrediction[0]}`);
        setPrediction(newPrediction);
        setCurrentPredictionIndex(0);
        setAttempt(0);
      }
    } else if (prediction.length > 0) {
      const currentResult = cards[cards.length - 1];
      if (currentResult === prediction[currentPredictionIndex]) {
        setPredictionType('waiting');
        // Win - enter wait mode and set start index
        setWaitMode(true);
        setWaitStartIndex(cards.length);
        setSignal('WIN - COLLECTING TWO NEW RESULTS');
        setPrediction([]);
        setCurrentPredictionIndex(0);
        setAttempt(0);
        return;
      } else {
        // Continue with sequence
        setAttempt(prev => prev + 1);
        if (currentPredictionIndex < prediction.length - 1) {
          setCurrentPredictionIndex(prev => prev + 1);
          setSignal(`BET ON ${prediction[currentPredictionIndex + 1]}`);
        } else {
          // Sequence complete without win
          setSignal('SEQUENCE COMPLETE - COLLECTING TWO NEW RESULTS');
          setPrediction([]);
          setCurrentPredictionIndex(0);
          setWaitMode(true);
          setWaitStartIndex(cards.length);
        }
      }
      updateSignalColor(attempt);
    }
  };

  const handlePlayerClick = () => {
    if (waitMode) {
      // In wait mode, only add new result
      const newCardList = [...cardList, 'PLAYER'];
      setCardList(newCardList);
      checkResult(newCardList);
    } else {
      // Normal mode
      const newCardList = [...cardList, 'PLAYER'];
      setCardList(newCardList);
      checkResult(newCardList);
    }
  };

  const handleBankerClick = () => {
    if (waitMode) {
      // In wait mode, only add new result
      const newCardList = [...cardList, 'BANKER'];
      setCardList(newCardList);
      checkResult(newCardList);
    } else {
      // Normal mode
      const newCardList = [...cardList, 'BANKER'];
      setCardList(newCardList);
      checkResult(newCardList);
    }
  };

  const isDeathSquare = (cards) => {
    if (cards.length < 4) return false;
    return cards[0] === cards[2] && cards[1] === cards[3];
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

  const handleReset = () => {
    setCardList([]);
    setAttempt(0);
    setSignal('');
    setSignalColor('normal');
    setPrediction([]);
    setCurrentPredictionIndex(0);
    setWaitMode(false);
    setWaitStartIndex(-1);
  };

  return (
    <div className="baccarat-container">
      <div className="header-buttons">
        <button className="back-button" onClick={onBack}>Back</button>
        <button className="reset-button" onClick={handleReset}>Reset</button>
      </div>
      <div className={`signal-box ${signalColor}`}>
        <span>{signal}</span>
      </div>
      <div className={`attempt-counter ${signalColor}`}>
        Attempt: {attempt}
      </div>
      <div className="controls">
        <button className="control-button" onClick={handlePlayerClick}>Player</button>
        <button className="control-button banker-button" onClick={handleBankerClick}>Banker</button>
      </div>
      <div className="results-grid">
        {cardList.map((card, index) => (
          <div key={index} className={`result-item ${card.toLowerCase()}`}>
            {card}
          </div>
        ))}
      </div>
      <div className="prediction-status">
        {prediction.length > 0 && (
          <div>
            Next Prediction: {prediction[currentPredictionIndex]}
            ({currentPredictionIndex + 1}/{prediction.length})
          </div>
        )}
      </div>
    </div>
  );
}

export default Baccarat;