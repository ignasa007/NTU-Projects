import { useState, useEffect, useRef } from "react";
import {
  getFlashcards,
  rescheduleFlashcard,
  getDueFlashcards,
  getFlashcardsByToday,
} from "../../services/flashcard-service";
import shuffle from "./shuffle";
import "./Flashcard.scss";
import draftToHtml from "draftjs-to-html";
import ConfettiGenerator from "confetti-js";

const StudyScreen = (props) => {
  const { token, history } = props;
  console.log(history);

  const dirStruct = history.location.state.dirStruc;
  const flashcardRef = useRef();
  const [flashcards, setFlashcards] = useState([
    { front: null, back: null, hints: null, title: null },
  ]);
  flashcardRef.current = flashcards;
  const [currentFlashcardId, setCurrentFlashcardId] = useState(0);
  const [back, setBack] = useState(flashcards[0].back);
  const [front, setFront] = useState(flashcards[0].front);
  const [hints, setHints] = useState(flashcards[0].hints);
  const [hintDisplayCounter, setHintDisplayCounter] = useState(0);
  const [isFront, setIsfront] = useState(true);
  const [title, setTitle] = useState(flashcards[0].title);
  const [endCard, setEndCard] = useState(false);

  const displayConfetti = () => {
    const confettiSettings = { target: "end-screen" };
    const confetti = new ConfettiGenerator(confettiSettings);
    confetti.render();

    setTimeout(function () {
      const element = document.getElementById("end-screen");
      element.remove();
    }, 1500);
  };

  useEffect(async () => {
    await updateFlashcards();
    setFront(flashcardRef.current[currentFlashcardId].front);
    setBack(flashcardRef.current[currentFlashcardId].back);
    setHints(flashcardRef.current[currentFlashcardId].hints);
    setTitle(flashcardRef.current[currentFlashcardId].title);
    setHintDisplayCounter(0);
    setIsfront(true);
    console.log(history.location.state);
  }, [currentFlashcardId]);

  const updateFlashcards = async () => {
    const flashcardsData = await getFlashcardsByToday(
      token,
      [history.location.state.path],
      dirStruct
    );
    console.log(flashcardsData);
    try {
      console.log("Updating...");
      if (flashcardsData.status === 200) {
        // setFlashcards(shuffle(flashcardsData.data.flashcardsContent));
        setFlashcards(flashcardsData.data.flashcardsContent);
        return flashcardsData.data.flashcardsContent;
      }
    } catch (err) {
      console.log(err);
    }
  };

  const displayHint = () => {
    return hintDisplayCounter == 0 ? (
      <></>
    ) : (
      hints
        .filter((element, index) => index < hintDisplayCounter)
        .map((element, index) => {
          return (
            <div key={index} id="hint-container" className="flex">
              <h1 id="hint-label" className="text-2xl">
                Hint {index + 1}: {element.content}
              </h1>
            </div>
          );
        })
    );
  };

  const handleNextCard = () => {
    if (currentFlashcardId < flashcards.length - 1) {
      setCurrentFlashcardId(currentFlashcardId + 1);
    } else setEndCard(true);
  };

  function lastEleOfArray(arr) {
    return arr[arr.length - 1];
  }

  const evalEasy = async () => {
    handleNextCard();
    await rescheduleFlashcard(token, flashcards[currentFlashcardId], "easy");
  };

  const evalMedium = async () => {
    handleNextCard();
    await rescheduleFlashcard(token, flashcards[currentFlashcardId], "medium");
  };

  const evalHard = async () => {
    handleNextCard();
    await rescheduleFlashcard(token, flashcards[currentFlashcardId], "hard");
  };

  return (
    <body className="App-body">
      <canvas id="end-screen" className="absolute"></canvas>
      {endCard ? (
        <div id="show-if-end-card">
          {displayConfetti()}
          <h1 className="mb-2">Congratulations!</h1>
          <h2>
            You have studied all the flashcards here.
            <br />
            Good job! &#128512;&#129351;
          </h2>
          <center>
            <button
              className="neutral-button mt-6 z-50"
              onClick={() => history.push("/home/folders")}
            >
              Return to Folders
            </button>
          </center>
        </div>
      ) : (
        <>
          <div id="header-container" className="-mt-4">
            <h1>
              Study Session:{" "}
              {lastEleOfArray(history.location.state.path.split("/"))}
            </h1>
            <center>
              <button
                className="neutral-button"
                type="button"
                onClick={() => history.push("/home/folders")}
              >
                Return to Folders
              </button>
            </center>
          </div>
          <div id="main-flashcard-container">
            {isFront ? (
              <>
                <div
                  id="flashcard-container"
                  className="flashcard-container mt-4"
                >
                  <h2 id="position-header">Front</h2>
                  <div id="main-content" className="text-left">
                    <div
                      dangerouslySetInnerHTML={{
                        __html: draftToHtml(JSON.parse(front)),
                      }}
                    />
                  </div>
                  <div id="divider" className="h-0.5 bg-gray-100 my-4"></div>
                  <button
                    id="show-hint-btn"
                    className="neutral-button"
                    onClick={() => {
                      setHintDisplayCounter(
                        Math.min(hintDisplayCounter + 1, hints.length)
                      );
                    }}
                  >
                    Show hint
                  </button>
                  <div id="hint-content">{displayHint()}</div>
                </div>
                <div className="flex justify-center mt-3">
                  <button
                    id="show-solution-btn"
                    className="warm-button"
                    onClick={() => {
                      setIsfront(false);
                    }}
                  >
                    Show solution
                  </button>
                </div>
              </>
            ) : (
              <>
                <div id="main-flashcard-container">
                  <div
                    id="flashcard-container"
                    className="flashcard-container mt-4"
                  >
                    <h2 id="position-header">Back</h2>
                    <div id="main-content" className="text-left">
                      <div
                        dangerouslySetInnerHTML={{
                          __html: draftToHtml(JSON.parse(back)),
                        }}
                      />
                    </div>
                  </div>
                  <div className="flex justify-center mt-3">
                    <div id="grading-container" className="flex justify-evenly">
                      <button
                        className="cold-button mx-4"
                        onClick={() => evalEasy()}
                      >
                        Easy (7 days)
                      </button>
                      <button
                        className="neutral-button mx-4"
                        onClick={() => evalMedium()}
                      >
                        Medium (4 days)
                      </button>
                      <button
                        className="warm-button mx-4"
                        onClick={() => evalHard()}
                      >
                        Hard (2 days)
                      </button>
                    </div>
                  </div>
                </div>
              </>
            )}
          </div>
        </>
      )}
    </body>
  );
};

export default StudyScreen;
