import api from "./api";

let loginUser = async (email, password) => {
  console.log("Logging in...");
  return api
    .post("/login", {
      username: email,
      password: password,
    })
    .then((res) => {
      if (res.status === 201) {
        console.log("Login successful.");
        return res;
      }
    })
    .catch((error) => {
      console.log(`Login failed: ${error.response.data.message}`);
      return `Error: ${error.response.data.message}`;
    });
};

let signUp = async (email, name, password) => {
  console.log("Signing up...");
  return api
    .post("/signUp", {
      email: email,
      name: name,
      preferred_username: name,
      password: password,
    })
    .then((res) => {
      if (res.status === 201) {
        console.log("Sign up successful.");
        return "SUCCESS";
      }
    })
    .catch((error) => {
      console.log(`Registration failed: ${error.response.data.message}`);
      return `Error: ${error.response.data.message}`;
    });
};

let forgotPassword = async (email) => {
  console.log("Sending Password");
  return api
    .post("/login/forgot-password", {
      username: email,
    })
    .then((res) => {
      if (res.status === 201) {
        console.log("Successful.");
        return "SUCCESS";
      } else {
        console.log("Failed.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
    });
};

let resetPassword = async (email, code, newPassword) => {
  console.log("Resetting Password");
  return api
    .post("/login/reset-password", {
      username: email,
      code: code,
      new_password: newPassword,
    })
    .then((res) => {
      if (res.status === 201) {
        console.log("Successful.");
        return "SUCCESS";
      } else {
        console.log("Failed.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
    });
};

let changePassword = async (email, oldPassword, newPassword) => {
  console.log("Changing Password");
  return api
    .post("/profile/change-password", {
      username: email,
      old_password: oldPassword,
      new_password: newPassword,
    })
    .then((res) => {
      if (res.status === 201) {
        console.log("Successful.");
        return "SUCCESS";
      } else {
        console.log("Failed.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
    });
};

export { loginUser };
export { signUp };
export { forgotPassword };
export { resetPassword };
export { changePassword };
