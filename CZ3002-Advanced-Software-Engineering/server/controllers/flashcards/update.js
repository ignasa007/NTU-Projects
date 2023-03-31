import startOfDay from "date-fns/startOfDay/index.js";
import flashcardModel from "../../models/flashcard.js";
import { process } from "../utils.js";
import { addDays } from "date-fns";
// update Flashcard Name, textFront, textback, hints, level, dueDate
export const updateFlashcard = async (req, res) => {
  if (!req.body) {
    return res.status(400).send({
      message: "Product content can not be empty",
    });
  }
  var { flashcard, path, dirStructure } = req.body;
  const { arr } = process(path);
  const flashcard_id = arr[arr.length - 1];
  flashcardModel
    .findByIdAndUpdate(
      flashcard._id,
      {
        title: flashcard.title,
        dueDate: flashcard.dueDate,
        level: flashcard.level,
        front: flashcard.front,
        back: flashcard.back,
        hints: flashcard.hints,
        revisionHistory: flashcard.revisionHistory,
      },
      { new: true }
    )
    .then((flashcard) => {
      console.log(flashcard);
      if (!flashcard) {
        return res.status(404).send({
          message: "Flashcard not found with ID " + req.params._id,
        });
      }
      res.send(flashcard);
    })
    .catch((err) => {
      if (err.kind === "ObjectId") {
        return res.status(404).send({
          message: "Flashcard not found with ID " + req.params._id,
        });
      }
      return res.status(500).send({
        message: "Something wrong updating FlashCardwith ID " + req.params._id,
      });
    });
};

export const rescheduleFlashcard = async (req, res) => {

  const { flashcard, difficulty } = req.body;
  console.log(flashcard, difficulty);

  flashcard.level += (difficulty == 'easy' ? 1 : (difficulty == 'hard' ? -1 : 0));
  flashcard.dueDate = addDays(startOfDay(new Date()), (difficulty == "easy" ? 7 : (difficulty == "medium" ? 4 : 2)));

  flashcard.revisionHistory.push({
    date: new Date(),
    level: flashcard.level,
    score: difficulty == "easy" ? 1 : (difficulty == "medium" ? 2 : 3),
  });

  console.log(flashcard);

  await flashcardModel.updateOne(
    {
      _id: flashcard._id,
    },
    {
      level: flashcard.level,
      dueDate: flashcard.dueDate,
      revisionHistory: flashcard.revisionHistory,
    }
  );

  res.status(204);
  
};
