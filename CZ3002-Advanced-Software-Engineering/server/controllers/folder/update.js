import { process } from "../utils.js";
import folderModel from "../../models/folder.js";

export const addFolder = async (req, res) => {

  var { path, dirStructure } = req.body;
  const { arr } = process(path);

  if (arr.length == 0) {
    let message = "Check the path. Couldn't parse.";
    console.log(message);
    res.status(400).json(Error({ message: message }));

  } else if (arr[0] === ".") {

    if (arr.length == 1) {
      console.log("Only root provided? Are you stupid?");
      res.status(400).json(Error({ message: "Check the path. Only root provided." }));

    } else {

      try {

        // if path = ./CZ3005/MDP/State Diagram, then command = "dirStructure['CZ3005']['MDP']"
        let command = "dirStructure";
        for (let i = 1; i < arr.length - 1; i++) {
          command = command + `['${arr[i]}']`;
        }

        if (eval(command) === null) {
          // if path = ./CZ3005/MDP/State Diagram, and MDP is null, set it to an empty object
          eval(command + " = {}");

        } else if (eval(command) instanceof Array) {
          // if path = ./CZ3005/MDP/State Diagram, and MDP has an array of flashcards IDs, error
          let message = `Folder ${arr.slice(0, -1).join("/")} is a base folder. Can't add folder '${arr[arr.length - 1]}' to base folder.`;
          console.log(message);
          res.status(400).json(Error({ message: message }));
          return;
        }

        // if path = ./CZ3005/MDP/State Diagram, then command = "dirStructure['CZ3005']['MDP']['State Diagram'] = null"
        command = command + `['${arr[arr.length - 1]}'] = null`;
        eval(command);

        console.log(`Added folder ${path}`);
        res.status(200).json(dirStructure);
        await folderModel.updateOne({ _id: req.params.id }, { contents: dirStructure });

      } catch (error) {
        console.log(error.stack);
        res.status(400).json(Error({ message: error.message }));
      }
    }

  } else {
    let message = "First folder should be root.";
    console.log(message);
    res.status(400).json(Error({ message: message }));
  }

};

export const addFlashcards = async (userID, dirStructure, relativePaths) => {

  relativePaths.forEach((path) => {

    let { arr } = process(path);

    if (arr.length == 0) {
      let message = `Check the path ${path}. Couldn't parse.`;
      console.log(message);

    } else if (arr[0] === ".") {

      if (arr.length == 1) {
        console.log("Only root provided? Are you stupid?");
        return Error({ message: "Check the path. Only root provided." });

      } else {

        try {

          // if path = ./CZ3005/MDP/123, then command = "dirStructure['CZ3005']['MDP']"
          let command = "dirStructure";
          for (let i = 1; i < arr.length - 2; i++) {
            command = command + `['${arr[i]}']`;
            let child = eval(command);
            if (typeof child == "undefined" || child === null) {
              eval(command + " = {}");
            }
          }

          command = command + `['${arr[arr.length - 2]}']`;
          let child = eval(command);

          if (typeof child == "undefined" || child === null) {
            // if path = ./CZ3005/MDP, and MDP is null, set it to an empty array
            eval(command + " = []");

          } else if (!(eval(command) instanceof Array)) {
            // if path = ./CZ3005/MDP/123, and MDP is not an array, error
            let message = `Folder ${arr.slice(0, -1).join("/")} is not a base folder. Can only add flashcard ${arr[arr.length - 1]} to a base folder.`;
            console.log(message);
          }

          // if path = ./CZ3005/MDP/State Diagram, then command = "dirStructure['CZ3005']['MDP'].push(123)"
          command = command + `.push('${arr[arr.length - 1]}')`;
          eval(command);
          console.log(`Added flashcard ${arr[arr.length - 1]} at ${path}.`);

        } catch (error) {
          console.log( `Encountered unexpected error adding flashcard ${arr[arr.length - 1]} at ${arr.slice(0, -1).join("/")}. ${error.stack}`);
        }

      }

    } else {
      let message = `First folder should be root but is not for path ${path}.`;
      console.log(message);

    }
  });

  await folderModel.updateOne({ _id: userID }, { contents: dirStructure });
  return dirStructure;

};

export const renameFolder = async (req, res) => {
  
  const { newName, path, dirStructure } = req.body;
  const { arr } = process(path);

  if (arr.length == 0) {
    let message = "Check the path. Couldn't parse.";
    console.log(message);
    res.status(400).json(Error({ message: message }));

  } else if (arr[0] === ".") {

    if (arr.length == 1) {
      console.log("Only root provided? Are you stupid?");
      res.status(400).json(Error({ message: "Check the path. Only root provided." }));

    } else {

      try {

        // if path = ./CZ3005/MDP/State Diagram, then command = "dirStructure['CZ3005']['MDP']"
        let command = "dirStructure";
        for (let i = 1; i < arr.length - 1; i++) {
          command = command + `['${arr[i]}']`;
        }

        // if path = ./CZ3005/MDP and rename = Intelligence, then command = "dirStructure['CZ3005']['Intelligence'] = dirStructure['CZ3005']['MDP']"
        eval(`${command}['${newName}'] = ${command}['${arr[arr.length - 1]}']`);
        // if path = ./CZ3005/MDP and rename = Intelligence, then command = "delete dirStructure['CZ3005']['MDP']"
        eval(`delete ${command}['${arr[arr.length - 1]}']`);

        console.log(`Renamed folder ${path} to ${newName}.`);
        res.status(200).json(dirStructure);
        await folderModel.updateOne({ _id: req.params.id }, { contents: dirStructure });

      } catch (error) {
        console.log(error.stack);
        res.status(500).json(Error({ message: error.message }));
      }
    }

  } else {
    let message = "First folder should be root.";
    console.log(message);
    res.status(400).json(Error({ message: message }));
  }

};

export const deleteFolder = async (req, res) => {

  var { path, dirStructure } = req.body;
  const { arr } = process(path);

  if (arr.length == 0) {
    let message = "Check the path. Couldn't parse.";
    console.log(message);
    res.status(400).json(Error({ message: message }));

  } else if (arr[0] === ".") {

    if (arr.length == 1) {
      console.log("Only root provided? Are you stupid?");
      res.status(400).json(Error({ message: "Check the path. Only root provided." }));

    } else {

      try {

        // if path = ./CZ3005/MDP, then command = "dirStructure['CZ3005']['MDP']"
        let command = "dirStructure";
        for (let i = 1; i < arr.length; i++) {
          command = command + `['${arr[i]}']`;
        }
        // if path = ./CZ3005/MDP, the command = "delete dirStructure['CZ3005']['MDP']"
        eval(`delete ${command}`);

        console.log(`Deleted folder ${path}`);
        res.status(200).json(dirStructure);
        await folderModel.updateOne({ _id: req.params.id }, { contents: dirStructure });

      } catch (error) {
        console.log(error.stack);
        res.status(500).json(Error({ message: error.message }));

      }

    }

  } else {
    let message = "First folder should be root.";
    console.log(message);
    res.status(400).json(Error({ message: message }));
  }

};
