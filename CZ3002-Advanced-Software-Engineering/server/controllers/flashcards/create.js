import flashcardModel from "../../models/flashcard.js";
import { addFlashcards } from "../folder/update.js";

export const createOneFlashcard = async (req, res) => {
  // ask if the user wants to revise the flashcard, if not flashcard.dueDate === null
  let { flashcard, path, dirStructure } = req.body;
  console.log(dirStructure);

  let { userFlashcards, updatedDirStructure } = await createFlashcards(
    req.params.id,
    dirStructure,
    [flashcard],
    [path],
    ""
  );
  console.log(updatedDirStructure);
  res.status(updatedDirStructure instanceof Error ? 500 : 201).json({
    userFlashcard: userFlashcards[0],
    updatedDirStructure: updatedDirStructure,
  });
};

export const createFlashcards = async (
  userID,
  dirStructure,
  flashcardsContent,
  absolutePaths
) => {
  let promises = [],
    flashcardPaths = [],
    userFlashcards = [];

  for (let i = 0; i < absolutePaths.length; i++) {
    let flashcard = flashcardsContent[i];
    let absolutePath = absolutePaths[i];

    flashcard.level = 1;
    if (typeof flashcard.dueDate == "undefined") {
      flashcard.dueDate = null;
    }
    flashcard.revisionHistory = [];

    try {
      let userFlashcard = new flashcardModel(flashcard);
      userFlashcards.push(userFlashcard);
      const promise = userFlashcard.save();
      promises.push(promise);
      flashcardPaths.push(absolutePath + "/" + (await promise).id);
    } catch (error) {
      console.log(
        `Encountered error making a new flashcard document. ${error.message}`
      );
    }
  }

  await Promise.all(promises);
  dirStructure = await addFlashcards(userID, dirStructure, flashcardPaths);
  console.log(dirStructure);

  return { userFlashcards: userFlashcards, updatedDirStructure: dirStructure };
};
