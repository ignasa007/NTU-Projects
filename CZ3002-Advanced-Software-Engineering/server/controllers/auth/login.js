import { Auth } from "aws-amplify";

export const login = async (req, res) => {
  const { username, password } = req.body;

  try {
    await Auth.signIn(username, password);
    let userAttr = await Auth.currentUserInfo().then((data) => {
      return data.attributes;
    });
    console.log(`Logging in user ${username}`);
    res.status(201).json(userAttr);
  } catch (error) {
    switch (error.name) {
      case "UserNotConfirmedException":
      case "NotAuthorizedException":
        console.log(`Error signing in. ${error.message}`);
        break;
      case "AuthError":
        console.log(
          "Error signing in. Username cannot be empty. Check and retry."
        );
      case "InvalidParameterEception":
        console.log(
          "Error signing in. Invalid password entered. Check and retry."
        );
        break;
      default:
        console.log(`Error signing in. ${error.name}: ${error.message}`);
    }
    res.status(401).json(error);
  }
};
