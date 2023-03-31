import api from "./api";

const getFlashcards = async (userID, path, dirStructure) => {
  console.log("Getting flashcards...");
  return api
    .get(`/flashcard/get-flashcards`, {
      params: {
        path: path,
        dirStructure: dirStructure,
      },
    })
    .then((res) => {
      if (res.status === 200) {
        return res;
      } else {
        console.log("Failed to get flashcard.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

const getDueFlashcards = async (userID, paths, dirStructure) => {
  console.log("Getting due flashcards...");
  return api
    .get(`/flashcard/study-mode/${userID}`, {
      params: {
        paths: paths,
        dirStructure: dirStructure,
      },
    })
    .then((res) => {
      if (res.status === 200) {
        console.log(`Response: ${res}`);
        return res;
      } else {
        console.log("Failed to get due flashcards.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

const getFlashcardsByToday = async(userID, path, dirStructure) => {

  return api
    .get(`/flashcard/study-mode`, { 
      params: {
        paths: [path],
        dirStructure: dirStructure
      }
    })
    .then((res) => {
      if (res.status === 200) {
        return res;
      } else {
        console.log("Failed to add flashcard.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });

}

const addFlashcard = async (userID, flashcard, path, dirStructure) => {
  console.log("Adding flashcard...");
  return api
    .post(`/flashcard/add-flashcard/${userID}`, {
      flashcard,
      /*
    "flashcard": {
        "dueDate": null,
        "textFront": "hello",
        "textBack": "world",
        "hints": [
            "!",
            ":)"
        ]
    }
    */
      path: path,
      dirStructure: dirStructure,
    })
    .then((res) => {
      if (res.status === 201) {
        console.log("Flashcard added.");
        return "SUCCESS";
      } else {
        console.log("Failed to add flashcard.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

const updateFlashcard = async (userID, flashcard, path, dirStructure) => {
  console.log("Updating flashcard...");
  return api
    .post(`/flashcard/update-flashcard/${userID}`, {
      flashcard,
      path: path,
      dirStructure: dirStructure,
    })
    .then((res) => {
      if (res.status === 201) {
        console.log("Flashcard updated.");
        return "SUCCESS";
      } else {
        console.log("Failed to update flashcard.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

const deleteFlashcard = async (userID, flashcard, path, dirStructure) => {
  console.log("Deleting flashcard...");
  return api
    .post(`/flashcard/delete-flashcard/${userID}`, {
      flashcard,
      path: path,
      dirStructure: dirStructure,
    })
    .then((res) => {
      if (res.status === 201) {
        console.log("Flashcard deleted.");
        return "SUCCESS";
      } else {
        console.log("Failed to delete flashcard.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

const exportFlashcards = async (userID, flashcard, path, dirStructure) => {
  console.log("Exporting flashcards...");
  return api
    .post(`/flashcard/export-flashcards/${userID}`, {
      flashcard,
      path: path,
      dirStructure: dirStructure,
    })
    .then((res) => {
      if (res.status === 201) {
        console.log("Flashcards exported.");
        return "SUCCESS";
      } else {
        console.log("Failed to export flashcards.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

const rescheduleFlashcard = async (userID, flashcard, difficulty) => {
  console.log("Updating flashcard...");
  console.log(userID);
  return api
    .post(`/flashcard/reschedule-flashcard/${userID}`, {
      flashcard,
      difficulty,
    })
    .then((res) => {
      if (res.status === 201) {
        console.log("Flashcard updated.");
        return "SUCCESS";
      } else {
        console.log("Failed to update flashcard.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

export { getFlashcards };
export { getFlashcardsByToday };
export { addFlashcard };
export { updateFlashcard };
export { deleteFlashcard };
export { exportFlashcards };
export { rescheduleFlashcard };
export { getDueFlashcards };
