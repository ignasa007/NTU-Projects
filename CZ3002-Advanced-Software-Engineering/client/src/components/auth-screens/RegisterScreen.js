import googlelogo from "../../assets/google-icon-logo.png";
import { Auth } from "aws-amplify";
import NavBar from "../NavBar";
import { signUp } from "../../services/auth-service";
import React, { useState } from "react";

const RegisterScreen = (props) => {
  const { history } = props;

  const handleNavigation = (screen) => {
    history.push(screen);
  };

  const [details, setDetails] = useState({
    name: "",
    email: "",
    password: "",
    password2: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const register = async (details) => {
    console.log(details);
    if (details.password != details.password2) {
      setError("Passwords do not match. Please try again.");
      return;
    }
    let status = await signUp(details.email, details.name, details.password);
    console.log(status);
    if (status === "SUCCESS") {
      setError("");
      setSuccess("Account successfully created. Please verify your email.");
    } else {
      setError(status);
    }
  };

  const submitHandler = (e) => {
    e.preventDefault();
    register(details);
  };

  const popupHandler = () => {
    var popup = document.getElementById("myPopup");
    popup.classList.toggle("show");
  };

  return (
    <div className="App">
      <body className="App-body">
        <h1>Register</h1>
        <div className="form-inner">
          <div class="mb-4">
            <button
              className="auth-button"
              type="button"
              onClick={() => Auth.federatedSignIn({ provider: "Google" })}
            >
              <img src={googlelogo} height="25rem" width="25rem" />
              <span>Continue with Google</span>
            </button>
          </div>
          <div className="subtle">
            <span>or</span>
          </div>
          <form onSubmit={submitHandler}>
            <div className="form-group">
              <label htmlFor="name">Name</label>
              <br />
              <input
                type="name"
                name="name"
                id="name"
                required="required"
                placeholder="Memoise"
                onChange={(e) =>
                  setDetails({ ...details, name: e.target.value })
                }
                value={details.name}
              />
            </div>
            <div className="form-group">
              <label htmlFor="email">Email</label>
              <br />
              <input
                type="email"
                name="email"
                id="email"
                required="required"
                placeholder="projectmemoise@gmail.com"
                onChange={(e) =>
                  setDetails({ ...details, email: e.target.value })
                }
                value={details.email}
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">Password</label>

              <link
                href="https://netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.css"
                rel="stylesheet"
              />
              <div class="popup ml-2" onClick={popupHandler}>
                <i class="icon-info-sign"></i>
                <span class="popuptext" id="myPopup">
                  Password must be 8-20 characters long, contain lowercase
                  letters, uppercase letters, special characters, and numbers.
                </span>
              </div>
              <br />
              <input
                type="password"
                name="password"
                id="password"
                required="required"
                placeholder="********"
                onChange={(e) =>
                  setDetails({ ...details, password: e.target.value })
                }
                value={details.password}
              />
            </div>
            <div className="form-group">
              <label htmlFor="password2">Re-enter password</label>
              <br />
              <input
                type="password"
                name="password2"
                id="password2"
                required="required"
                placeholder="********"
                onChange={(e) =>
                  setDetails({ ...details, password2: e.target.value })
                }
                value={details.password2}
              />
            </div>
            {error != "" ? <div className="error">{error}</div> : ""}
            {success != "" ? <div className="success">{success}</div> : ""}
            <input type="submit" value="Let's get started!" />
          </form>
        </div>
        <a href="#" onClick={() => handleNavigation("/login")}>
          Already have an account? Login here.
        </a>
      </body>
    </div>
  );
};

export default RegisterScreen;
