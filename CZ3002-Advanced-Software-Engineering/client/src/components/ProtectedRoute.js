import React from "react";
import { Route, Redirect } from "react-router-dom";

const ProtectedRoute = ({ component: Component, ...rest }) => {
  let isAuthenticated = localStorage.getItem("token");
  return (
    <Route
      {...rest}
      render={(props) => {
        isAuthenticated = localStorage.getItem("token");
        if (isAuthenticated) {
          console.log(isAuthenticated);
          return <Component {...props} {...rest} />;
        } else {
          console.log(isAuthenticated);
          return (
            <Redirect
              to={{
                pathname: "/login",
                state: {
                  from: props.location,
                },
              }}
            />
          );
        }
      }}
    />
  );
};

export default ProtectedRoute;
