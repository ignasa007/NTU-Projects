import { Auth } from "aws-amplify";

export const signUp = async (req, res) => {
  const { email, name, preferred_username, password } = req.body;
  const username = email;
  try {
    const { post } = await Auth.signUp({
      username,
      password,
      attributes: {
        email,
        name,
        preferred_username,
      },
    });
    console.log(`Sent verification link for user ${name} at ${email}.`);
    res.status(201).json("Verification sent.");
  } catch (error) {
    switch (error.name) {
      case "UsernameExistsException":
        console.log(`A user with email ${email} already exists.`);
        break;
      default:
        console.log(`Error signing up. ${error.name}: ${error.message}.`);
    }
    res.status(409).json(error);
  }
};
