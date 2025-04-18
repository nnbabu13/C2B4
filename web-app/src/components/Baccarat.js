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
  const [results, setResults] = useState([]); // Add this for Big Road

  const getPredictionSequence = (card1, card2) => {
    if (card1 === 'BANKER' && card2 === 'BANKER') return ['BANKER', 'PLAYER', 'PLAYER', 'PLAYER'];
    if (card1 === 'PLAYER' && card2 === 'PLAYER') return ['PLAYER', 'BANKER', 'BANKER', 'BANKER'];
    if (card1 === 'BANKER' && card2 === 'PLAYER') return ['BANKER', 'BANKER', 'PLAYER', 'BANKER'];
    if (card1 === 'PLAYER' && card2 === 'BANKER') return ['PLAYER', 'PLAYER', 'BANKER', 'PLAYER'];
    return [];
  };

  // Add this new state near the top with other state declarations
  const [resultStatus, setResultStatus] = useState([]);
  
  // Modify the checkResult function to track result status
  // Modify the checkResult function
  const checkResult = (cards) => {
    if (waitMode) {
      setPredictionType('waiting');
      const postWinResults = cards.slice(waitStartIndex);
      
      // Add 'C' for any result during wait mode
      setResultStatus(prev => [...prev, 'C']);
      
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
        // Add 'C' for initial two results
        setResultStatus(prev => [...prev, 'C', 'C']);
      }
    } else if (prediction.length > 0) {
      const currentResult = cards[cards.length - 1];
      
      // Check if current result matches prediction
      if (currentResult === prediction[currentPredictionIndex]) {
        setPredictionType('waiting');
        setWaitMode(true);
        setWaitStartIndex(cards.length);
        setSignal('WIN - COLLECTING TWO NEW RESULTS');
        setPrediction([]);
        setCurrentPredictionIndex(0);
        setAttempt(0);
        setResultStatus(prev => [...prev, 'W']);
      } else {
        setAttempt(prev => prev + 1);
        setResultStatus(prev => [...prev, 'L']);
        
        if (currentPredictionIndex < prediction.length - 1) {
          setCurrentPredictionIndex(prev => prev + 1);
          setSignal(`BET ON ${prediction[currentPredictionIndex + 1]}`);
        } else {
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
    setResults([]);
    setAttempt(0);
    setSignal('');
    setSignalColor('normal');
    setPrediction([]);
    setCurrentPredictionIndex(0);
    setWaitMode(false);
    setWaitStartIndex(-1);
    setResultStatus([]); // Reset result status
  };

  const renderBigRoad = () => {
    const columns = [];
    let currentColumn = [];
    
    cardList.forEach((result, index) => {
      if (index === 0) {
        currentColumn.push(result);
      } else {
        const prevResult = cardList[index - 1];
        
        if (result !== prevResult || currentColumn.length >= 6) {
          columns.push([...currentColumn]);
          currentColumn = [result];
        } else {
          currentColumn.push(result);
        }
      }
    });
    
    if (currentColumn.length > 0) {
      columns.push(currentColumn);
    }

    return (
      <div className="big-road-container">
        <div className="big-road">
          {columns.map((column, colIndex) => (
            <div key={colIndex} className="big-road-column">
              {Array(6).fill(null).map((_, rowIndex) => (
                <div key={`${colIndex}-${rowIndex}`}>
                  {rowIndex < column.length ? (
                    <div className={`big-road-cell ${column[rowIndex].toLowerCase()}`}>
                      {column[rowIndex] === 'BANKER' ? 'B' : 'P'}
                    </div>
                  ) : (
                    <div className="big-road-empty" />
                  )}
                </div>
              ))}
            </div>
          ))}
        </div>
      </div>
    );
  };

  return (
    <div className="baccarat-container">
      <div className="header-buttons">
        <button className="back-button" onClick={onBack}>Back</button>
        <button className="reset-button" onClick={handleReset}>Reset</button>
      </div>
      
      {renderBigRoad()} {/* Add Big Road display here */}
      
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
        {resultStatus.map((status, index) => (
          <div key={index} className={`result-item ${status.toLowerCase()}`}>
            {status}
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