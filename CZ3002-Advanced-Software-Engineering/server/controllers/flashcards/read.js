import endOfDay from "date-fns/endOfDay/index.js";
import mongoose from "mongoose";

import flashcardsModel from "../../models/flashcard.js";
import { process } from "../utils.js";


export const getBaseFolderFlashcards = async (req, res) => {

  let { path, dirStructure } = req.query;
  dirStructure = JSON.parse(dirStructure);

  let { arr, processedPath } = process(path);
  var flashcardsContent = [], relativePaths = [];

  if (arr.length == 0) {

    let message = "Check the path. Couldn't parse.";
    console.log(message);
    res.status(400).json(Error({ message: message }));

  } else if (arr[0] === ".") {

    try {

      // if path = ./CZ3005/MDP, then command = "dirStructure['CZ3005']['MDP']"
      let folder = null;
      let command = "folder = dirStructure";
      for (let i=1; i<arr.length; i++) {
        command = command + `['${arr[i]}']`;
      }
      eval(command);

      const { contents, paths } = await getFlashcardsByFolder(folder, processedPath.slice(2));
      flashcardsContent.push(...contents);
      relativePaths.push(...paths);

      res.status(200).json({
        flashcardsContent: flashcardsContent,
        relativePaths: relativePaths,
      });

    } catch (error) {

      console.log(error.stack);
      res.status(500).json(Error({ message: error.message }));

    }

  } else {

    let message = `First folder should be root. Invalid path ${path}.`;
    console.log(message);
    res.status(400).json(Error({ message: message }));

  }

}


export const getFlashcardsCramMode = async (req, res) => {

  let { paths, dirStructure } = req.query;
  dirStructure = JSON.parse(dirStructure);
  paths = JSON.parse(paths);
  let dueDate = null;

  let returnValue = await getFlashcardsByDueDate(paths, dirStructure, dueDate);
  res.status(returnValue instanceof Error ? 500 : 200).json(returnValue);

}


export const getFlashcardsStudyMode = async (req, res) => {

  let { paths, dirStructure } = req.query;
  dirStructure = JSON.parse(dirStructure);
  let dueDate = new Date();

  let returnValue = await getFlashcardsByDueDate(paths, dirStructure, dueDate);
  res.status(returnValue instanceof Error ? 500 : 200).json(returnValue);

}


async function getFlashcardsByDueDate(paths, dirStructure, dueDate) {

  console.log(paths, dirStructure, dueDate)

  var flashcardsContent = [], relativePaths = [];

  for (let path of paths) {
    console.log(path)

    let { arr, processedPath } = process(path);

    if (arr.length == 0) {

      let message = "Check the path. Couldn't parse.";
      console.log(message);
      return Error({ message: message });

    } else if (arr[0] === ".") {

      try {

        // if path = ./CZ3005/MDP, then command = "dirStructure['CZ3005']['MDP']"
        let folder = null;
        let command = "folder = dirStructure";
        for (let i = 1; i < arr.length; i++) {
          command = command + `['${arr[i]}']`;
        }
        eval(command);

        const { contents, paths } = await getFlashcardsByFolder(folder, processedPath.slice(2), dueDate);
        flashcardsContent.push(...contents);
        relativePaths.push(...paths);

      } catch (error) {

        console.log(error.stack);
        return Error({ message: error.message });

      }

    } else {

      let message = `First folder should be root. Invalid path ${path}.`;
      console.log(message);
      return Error({ message: message });

    }

  }

  return {
    flashcardsContent: flashcardsContent,
    relativePaths: relativePaths,
  };

}


export async function getFlashcardsByFolder(folder, path, dueDate=null) {

  let flashcardsContent = [], relativePaths = [];

  if (folder === null) {

    // if empty folder, return empty array
    return {
      contents: flashcardsContent,
      paths: relativePaths,
    };

  } else if (folder instanceof Array) {

    // if base folder, retrieve flashcards from collection using id
    let flashcardIDs = folder.map(function (id) {
      return mongoose.Types.ObjectId(id);
    });

    let filter = (dueDate == null)
        ? { _id: { $in: flashcardIDs } }
        : { _id: { $in: flashcardIDs }, dueDate: { $lt: endOfDay(dueDate) } };

    return await flashcardsModel.find(filter).then((allFlashcards) => {

      allFlashcards.forEach(function (content) {
        if (!(content instanceof Error) && content !== null) {
          flashcardsContent.push(content);
          relativePaths.push(path + "/" + content.title);
        }
      });

      return {
        contents: flashcardsContent,
        paths: relativePaths,
      };

    });

  } else {

    // if not empty folder and not base folder, retrieve flashcards form all subfolders
    for (let key of Object.keys(folder)) {
      let { contents, paths } = await getFlashcardsByFolder(folder[key], (path == '' ? path : path + '/') + key, dueDate);
      flashcardsContent.push(...contents);
      relativePaths.push(...paths);
    }

    return {
      contents: flashcardsContent,
      paths: relativePaths,
    };

  }

}