import mongoose from "mongoose";

const flashcardModel = mongoose.model(
  "flashcard",
  mongoose.Schema({
    title: {
      type: String,
      required: true,
    },
    dueDate: {
      type: Date,
      default: null,
    },
    level: {
      type: Number,
      default: 1,
    },
    front: {
      type: String,
      default: "",
    },
    back: {
      type: String,
      default: "",
    },
    hints: {
      type: [Object],
      default: "",
    },
    revisionHistory: {
      type: [
        {
          date: Date,
          level: Number,
          score: Number,
        },
      ],
      default: [],
    },
  })
);

export default flashcardModel;
