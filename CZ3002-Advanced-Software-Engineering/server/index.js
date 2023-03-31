import express from "express";
import Amplify from "aws-amplify";
import mongoose from "mongoose";
import cors from "cors";
// import dotenv from 'dotenv';

import config from "./config.js";
import signUpRoutes from "./routes/signUp.js";
import loginRoutes from "./routes/login.js";
import folderRoutes from "./routes/folder.js";
import flashcardRoutes from "./routes/flashcard.js";
import profileRoutes from "./routes/profile.js";

const app = express();
// dotenv.config();

app.use(express.json({}));
app.use(express.urlencoded({ extended: true }));
app.use(cors());

app.use("/signUp", signUpRoutes);
app.use("/login", loginRoutes);
app.use("/folders", folderRoutes);
app.use("/flashcard", flashcardRoutes);
app.use("/profile", profileRoutes);

// const CONNECTION_URL = JSON.parse(process.env.mongo).uri;
const CONNECTION_URL = config.mongo.uri;
const PORT = process.env.PORT || 5000;

// Amplify.default.configure(JSON.parse(process.env.cognito));
Amplify.default.configure(config.cognito);

mongoose
  .connect(CONNECTION_URL, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() =>
    app.listen(PORT, () => console.log(`Server running on port: ${PORT}`))
  )
  .catch((error) => console.log(`${error} did not connect`));
