import express from "express";
import { getFolders } from "../controllers/folder/read.js";
import {
  addFolder,
  renameFolder,
  deleteFolder,
} from "../controllers/folder/update.js";

var router = express.Router();

router.get("/:id", getFolders);
router.put("/add-folder/:id", addFolder);
router.put("/rename-folder/:id", renameFolder);
router.put("/delete-folder/:id", deleteFolder);

export default router;
