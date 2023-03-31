// reference: https://www.digitalocean.com/community/tutorials/how-to-add-login-authentication-to-react-applications

import { useState } from "react";

export default function useToken() {
  const getToken = () => {
    const tokenString = localStorage.getItem("token");
    const userToken = tokenString;
    return userToken;
  };

  const [token, setToken] = useState(getToken());

  const saveToken = (userToken) => {
    localStorage.setItem("token", userToken);
    setToken(userToken);
  };

  return {
    token: localStorage.getItem("token"),
    setToken: saveToken,
  };
}
