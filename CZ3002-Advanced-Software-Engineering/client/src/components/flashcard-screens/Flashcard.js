import { useEffect, useState } from "react";
import { convertFromRaw, EditorState, convertToRaw } from "draft-js";
import { Editor } from "react-draft-wysiwyg";
import "react-draft-wysiwyg/dist/react-draft-wysiwyg.css";
import "../../App.scss";

import axios from "axios";

const Flashcard = (props) => {
  const { setFlashcards, flashcards, currentFlashcard, path, dirStruc } = props;

  useEffect(() => {
    setHints(currentFlashcard.hints);
    if (currentFlashcard.front.length) {
      setPromptState(() =>
        EditorState.createWithContent(
          convertFromRaw(JSON.parse(currentFlashcard.front))
        )
      );
    } else {
      setPromptState(() => EditorState.createEmpty());
    }
    if (currentFlashcard.back.length) {
      setSolutionState(() =>
        EditorState.createWithContent(
          convertFromRaw(JSON.parse(currentFlashcard.back))
        )
      );
    } else {
      setSolutionState(() => EditorState.createEmpty());
    }
    setIsFront(true);
  }, [currentFlashcard]);

  const [promptState, setPromptState] = useState(() =>
    EditorState.createEmpty()
  );

  const [solutionState, setSolutionState] = useState(() =>
    EditorState.createEmpty()
  );

  const [hints, setHints] = useState(currentFlashcard.hints);
  const [isFront, setIsFront] = useState(true);
  const handleAddHints = () => {
    setHints([...hints, { index: hints.length, content: "" }]);
  };

  const handleDeleteHints = (index) => {
    setHints([...hints.filter((hint) => hint.index != index)]);
  };

  const handleAddFlashcard = () => {
    const flashcard = {
      title: currentFlashcard.title,
      _id: currentFlashcard._id,
      dueDate: currentFlashcard.dueDate,
      level: currentFlashcard.level,
      front: JSON.stringify(convertToRaw(promptState.getCurrentContent())),
      hints: hints,
      back: JSON.stringify(convertToRaw(solutionState.getCurrentContent())),
      revisionHistory: currentFlashcard.revisionHistory,
    };
    let tempCopy = flashcards.slice();
    let currPosition = flashcards.indexOf(currentFlashcard);
    tempCopy.splice(currPosition, 1, flashcard);
    setFlashcards(tempCopy);
    axios({
      url: `http://localhost:5000/flashcard/update-flashcard`, // maybe add userID
      method: "PUT",
      data: {
        flashcard: flashcard,
        path: path + "/" + currentFlashcard._id,
        dirStructure: dirStruc,
      },
    })
      .then((value) => console.log("flashcard updated!", value))
      .catch((err) =>
        console.log("error occured while updating flashcard!", err)
      );
    alert("Flashcard Added!");
  };

  const handleEditHints = (e, index) => {
    const updatedHint = { index: index, content: e.target.value };
    setHints([...hints.filter((hint) => hint.index != index), updatedHint]);
  };

  const displayHints = () => {
    return hints.map((element, index) => {
      return (
        <div key={index} id="hint-container" className="flex">
          <h3 id="hint-label" className="mt-4 ml-4">
            Hint {index + 1}:
          </h3>
          <textarea
            className="m-2 border-2 rounded-md w-screen max-w-screen-md m-4 h-16 border-gray-300"
            placeholder="(Optional Hint)"
            value={element.content}
            onChange={(e) => handleEditHints(e, index)}
          ></textarea>
          <button
            className="neutral-button mt-5"
            onClick={() => handleDeleteHints(element.index)}
          >
            X
          </button>
        </div>
      );
    });
  };

  return (
    <div>
      {isFront ? (
        <div
          id="main-flashcard-container"
          className="bg-white border-2 rounded-xl p-4 pt-2 h-auto"
        >
          <h3 id="position-header" className="text-left">
            Front
          </h3>
          <div id="main-content-container">
            <Editor
              editorState={promptState}
              onEditorStateChange={setPromptState}
              wrapperClassName="wrapper-class"
              editorClassName="editor-class"
              toolbarClassName="toolbar-class"
            />
          </div>
          <div className="flex-col flex justify-center">
            <button
              id="add-hint-button"
              className="neutral-button"
              onClick={handleAddHints}
            >
              Add Hint
            </button>
            <div id="hint-container">{displayHints()}</div>
            <button onClick={() => setIsFront(!isFront)}>Swap to Back</button>
          </div>
        </div>
      ) : (
        <div
          id="main-flashcard-container"
          className="bg-white border-2 rounded-xl p-4 pt-2"
        >
          <h3 id="position-header" className="text-left">
            Back
          </h3>
          <div id="main-content-container">
            <Editor
              editorState={solutionState}
              onEditorStateChange={setSolutionState}
              wrapperClassName="wrapper-class"
              editorClassName="editor-class"
              toolbarClassName="toolbar-class"
            />
          </div>

          <div className="flex-col flex justify-center">
            <button onClick={() => setIsFront(!isFront)}>Swap to Front</button>
          </div>
        </div>
      )}
      <div className="flex justify-center m-4">
        <button
          id="confirm-button"
          className="warm-button"
          onClick={handleAddFlashcard}
        >
          CONFIRM
        </button>
      </div>
    </div>
  );
};

export default Flashcard;
