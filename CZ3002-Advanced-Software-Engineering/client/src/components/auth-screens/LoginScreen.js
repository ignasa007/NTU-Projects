import { withRouter } from "react-router";
import googlelogo from "../../assets/google-icon-logo.png";
import { Auth } from "aws-amplify";
import { loginUser } from "../../services/auth-service";
import React, { useState } from "react";
import PropTypes from "prop-types";

const LoginScreen = (props) => {
  const { history } = props;

  const handleNavigation = (screen) => {
    history.push(screen);
  };

  const [details, setDetails] = useState({ email: "", password: "" });

  const [error, setError] = useState("");

  const setToken = props.setToken;

  const login = async (details) => {
    const userDetails = await loginUser(details.email, details.password);
    if (userDetails.status === 201) {
      setToken(userDetails.data.sub);
      localStorage.setItem("token", userDetails.data.sub);
      handleNavigation("/home/folders");
    } else {
      setError(userDetails);
    }
  };

  const handleFedLogin = async () => {
    await Auth.federatedSignIn({ provider: "Google" }).then();
    let user = await Auth.currentAuthenticatedUser();
    localStorage.setItem("user", user);
  };

  const submitHandler = (e) => {
    e.preventDefault();
    login(details);
  };

  return (
    <div className="App">
      <body className="App-body">
        <h1>Login</h1>
        <div className="form-inner">
          <div class="mb-4">
            <button
              className="auth-button"
              type="button"
              onClick={() => {
                handleFedLogin();
              }}
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

            {error != "" ? <div className="error">{error}</div> : ""}
            <input type="submit" value="Welcome back!" />
          </form>
        </div>
        <a href="#" onClick={() => handleNavigation("/register")}>
          No account? Register here.
        </a>
      </body>
    </div>
  );
};

LoginScreen.propTypes = {
  setToken: PropTypes.func.isRequired,
};

export default withRouter(LoginScreen);
