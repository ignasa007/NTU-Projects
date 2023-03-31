import { useState, useEffect } from "react";
import { withRouter } from "react-router";
import "./Flashcard.scss";
import Flashcard from "./Flashcard";
import axios from "axios";

const timeElapsed = Date.now();
const dateToday = new Date(timeElapsed);

const FlashcardScreen = (props) => {
  const { history } = props;
  const [flashcards, setFlashcards] = useState([]);
  const [flashcardCount, setFlashcardCount] = useState(0);
  const [selectedFlashcard, setSelectedFlashcard] = useState();
  const [isAddingFlashcard, setIsAddingFlashcard] = useState(false);
  const [path, setPath] = useState();
  const [dirStruc, setDirStruc] = useState();
  const [userID, setUserID] = useState();
  const [mode, setMode] = useState(true);

  useEffect(() => {
    console.log(mode);
    setPath(history?.location?.state?.path);
    setDirStruc(history?.location?.state?.dirStruc);
    setUserID(history?.location?.state?.userID);
    // console.log(history.location.state)
    axios
      .get(`http://localhost:5000/flashcard/get-flashcards`, {
        params: {
          path: history?.location?.state?.path,
          dirStructure: history?.location?.state?.dirStruc,
        },
      })
      // .get(`http://localhost:5000/flashcard/cram-mode`, {
      //   params: {
      //     paths: ['./SAMAY', './HelloWorld'],
      //     dirStructure: history?.location?.state?.dirStruc,
      //   },
      // })
      .then((value) => {
        let flashcards = value.data.flashcardsContent;
        console.log("THESE ARE MY FLASHCARDS: ", flashcards);
        setFlashcards(flashcards);
        setFlashcardCount(flashcards.length);
      })
      .catch((err) => console.log(err));
  }, []);

  const handleDeleteFlashcard = (toDelete) => {
    console.log(history.location.state.path + "/" + toDelete);
    console.log(history.location.state.dirStruc);
    axios({
      url: `http://localhost:5000/flashcard/delete-flashcard/${userID}`,
      method: "DELETE",
      data: {
        path: history.location.state.path + "/" + toDelete,
        dirStructure: history.location.state.dirStruc,
      },
    })
      .then((value) => {
        console.log("flashcard has been deleted: ", value);
        setFlashcards([
          ...flashcards.filter((flashcard) => flashcard._id != toDelete),
        ]);
        setFlashcardCount(flashcardCount - 1);
      })
      .catch((err) =>
        console.log("error occured while deleting flashcard: ", err)
      );
  };

  const handleNavigation = (screen, params) => {
    history.push({ pathname: screen, state: params });
  };

  const displayFlashcards = (flashcards) => {
    return flashcards.map((element, id) => {
      return (
        <div key={id} id="flashcard">
          {element == selectedFlashcard ? (
            <div className="flex justify-center">
              <button
                className="neutral-button"
                onClick={() => handleDeleteFlashcard(element._id)}
              >
                <h2 className="hover:text-red-600">X</h2>
              </button>
            </div>
          ) : null}
          <button
            id="template-content"
            className={
              element == selectedFlashcard
                ? "bg-white border-2 border-yellow-300 w-48 h-32"
                : "bg-white border-2 w-48 h-32"
            }
            onClick={() => setSelectedFlashcard(element)}
          >
            <h2>{element.title.slice(0, 30)}</h2>
          </button>
        </div>
      );
    });
  };

  const handleAddFlashcard = (e) => {
    if (e.keyCode === 13) {
      setIsAddingFlashcard(false);
      const flashcardToAdd = {
        title: e.target.value,
        dueDate: dateToday,
        front: "",
        back: "",
        hints: [],
      };
      console.log(dirStruc);

      axios({
        url: `http://localhost:5000/flashcard/add-flashcard/${userID}`,
        method: "POST",
        data: {
          flashcard: flashcardToAdd,
          path: path,
          dirStructure: dirStruc,
        },
      })
        .then((res) => {
          let { userFlashcard, updatedDirStructure } = res.data;
          setFlashcardCount(flashcardCount + 1);
          setFlashcards([...flashcards, userFlashcard]);
          console.log("flashcard has been successfully added: ", userFlashcard);
          console.log("updated dirStruc: ", updatedDirStructure);
          setDirStruc(updatedDirStructure);
        })
        .catch((err) =>
          console.log("error occured while adding flashcard: ", err)
        );
    }
  };

  const displaySelectedFlashcard = (selectedFlashcard) => {
    return (
      <Flashcard
        setFlashcards={setFlashcards}
        flashcards={flashcards}
        currentFlashcard={selectedFlashcard}
        path={path}
        dirStruc={dirStruc}
      />
    );
  };

  return (
    <body className="App-body">
      <div id="main-container">
        <div
          id="header-container"
          className="flex -mt-2 mb-2 justify-between w-screen"
        >
          <button
            className="neutral-button mx-8"
            onClick={() => handleNavigation(`/home/folders`)}
          >
            Back to Folders
          </button>
          <h1 id="header-text" className="">
            {path ? path.split("/").reverse()[0] : ""}
          </h1>
          <div id="buttons-container" className="flex flex-row">
            <p>Choose Mode: </p>

            <input
              type="radio"
              id="Normal"
              name="Mode"
              value="Normal"
              onChange={(e) => setMode(e.target.value)}
              checked={mode}
            />
            <label for="Normal">Normal</label>
            <input type="radio" id="Shuffled" name="Mode" value="Shuffled" />
            <label for="Shuffled">Shuffled</label>
            {console.log(mode)}
            <button
              className="warm-button mx-4"
              onClick={() =>
                handleNavigation(`/home/${path.substring(2)}/study`, {
                  path: path,
                  dirStruc: dirStruc,
                  userID: userID,
                })
              }
            >
              Study
            </button>
            <button
              className="warm-button mr-8"
              onClick={() =>
                handleNavigation(`/home/${path.substring(2)}/cram`, {
                  path: path,
                  dirStruc: dirStruc,
                  userID: userID,
                })
              }
            >
              Cram
            </button>
          </div>
        </div>
        <div id="divider" />
        <div id="main-flashcard-container" className="flex w-screen">
          <div id="flashcard-list-container" className="sidebar ml-4">
            <center>
              <button
                id="add-flashcard-button"
                className="cold-button m-4"
                onClick={() => setIsAddingFlashcard(true)}
              >
                Add Flashcard
              </button>
            </center>
            {isAddingFlashcard ? (
              <input
                className="mt-4"
                onKeyDown={(e) => handleAddFlashcard(e)}
                onBlur={() => setIsAddingFlashcard(false)}
                autoFocus={true}
              ></input>
            ) : null}
            <div
              id="flashcard-templates-container"
              className="flashcard-list space-y-6 mt-4 p-4 overflow-y-scroll overflow-y-hidden w-60"
            >
              {displayFlashcards(flashcards)}
            </div>
          </div>
          <div id="add-flashcard-container" className="flex ml-24 my-4 w-8/12">
            {typeof selectedFlashcard !== "undefined"
              ? displaySelectedFlashcard(selectedFlashcard)
              : null}
          </div>
        </div>
      </div>
    </body>
  );
};

export default withRouter(FlashcardScreen);
