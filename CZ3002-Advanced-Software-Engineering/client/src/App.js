import "./App.scss";
import NavBar from "./components/NavBar.js";
import ProtectedNavBar from "./components/ProtectedNavBar.js";
import { Switch, Route, BrowserRouter } from "react-router-dom";
import WelcomeScreen from "./components/auth-screens/WelcomeScreen";
import RegisterScreen from "./components/auth-screens/RegisterScreen";
import LoginScreen from "./components/auth-screens/LoginScreen";
import FolderScreen from "./components/folder-screens/FolderScreen";
import FlashcardScreen from "./components/flashcard-screens/FlashcardScreen";
import SelectFoldersScreen from "./components/select-folders/SelectFoldersScreen";
import ProtectedRoute from "./components/ProtectedRoute";
import StudyScreen from "./components/flashcard-screens/StudyScreen";
import React, { useState } from "react";
import useToken from "./components/Token";
import CramScreen from "./components/flashcard-screens/CramScreen";

function App() {
  const { token, setToken } = useToken();

  const LoginContainer = () => {
    return (
      <div>
        {localStorage.getItem("token") ? <ProtectedNavBar /> : <NavBar />}
        <Switch>
          <Route exact path="/" component={WelcomeScreen} />
          <Route
            exact
            path="/login"
            component={(props) => (
              <LoginScreen setToken={setToken} {...props} />
            )}
          />
          <Route exact path="/register" component={RegisterScreen} />
        </Switch>
      </div>
    );
  };

  const FeaturesContainer = () => {
    return (
      <div>
        <Switch>
          <ProtectedRoute
            exact
            path="/home/folders"
            component={FolderScreen}
            token={token}
          />
          <ProtectedRoute
            exact
            path="/home/folders/*"
            component={FolderScreen}
            token={token}
          />
          <ProtectedRoute
            exact
            path="/home/*-flashcards"
            component={FlashcardScreen}
            token={token}
          />
          <ProtectedRoute
            exact
            path="/home/*/study"
            component={StudyScreen}
            token={token}
          />
          <ProtectedRoute
            exact
            path="/home/*/cram"
            component={CramScreen}
            token={token}
          />
          {/* <ProtectedRoute
            exact
            path="/home/select"
            component={SelectFoldersScreen}
          /> */}
          <Route path="*" component={() => "404 NOT FOUND"} />
        </Switch>
      </div>
    );
  };

  return (
    <div className="App">
      <BrowserRouter>
        <Route component={LoginContainer} />
        <ProtectedRoute exact path="/home/*" component={FeaturesContainer} />
      </BrowserRouter>
    </div>
  );
}

export default App;
