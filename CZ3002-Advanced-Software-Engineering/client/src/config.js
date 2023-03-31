const config = {
  cognito: {
    aws_project_region: "us-east-1",
    aws_cognito_region: "us-east-1",
    aws_user_pools_id: "us-east-1_2sEbwjNY2",
    aws_user_pools_web_client_id: "4g3dh25jlfk3ka9jbufbj2i7b3",
    mandatorySignIn: true,
    oauth: {
      domain: "memoise.auth.us-east-1.amazoncognito.com",
      scope: ["openid", "email", "profile"],
      redirectSignIn: "http://localhost:3000/login",
      redirectSignOut: "http://localhost:3000/login",
      responseType: "token",
    },
    federationTarget: "COGNTIO_USER_POOLS",
  },
};

export default config;
