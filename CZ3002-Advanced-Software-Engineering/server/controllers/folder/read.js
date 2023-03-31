import folderModel from "../../models/folder.js";
import createNewUser from "./create.js";
import config from "../../config.js";
import mongoose from "mongoose";

// let CONNECTION_URL = config.mongo.uri
// var PORT=5000
// await mongoose.connect(CONNECTION_URL);

async function readFolders(userID) {
  try {
    var data = await folderModel.findById(userID);
    if (data === null) {
      console.log("User not found. Making a new document.");
      data = createNewUser(userID);
    }
    return data;
  } catch (error) {
    console.log(error.message);
    return error;
  }
}

export default readFolders;

export const getFolders = async (req, res) => {
  const userID = req.params.id;

  try {
    let data = await readFolders(userID);
    console.log(`Successfully retrieved folders for user ${userID}`);
    res.status(201).json(data.contents);
  } catch (error) {
    console.log(`Error retrieving folders. ${error.name}: ${error.message}`);
    res.status(401).json(error);
  }
};
