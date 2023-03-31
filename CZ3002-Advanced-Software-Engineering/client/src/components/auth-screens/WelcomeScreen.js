import { withRouter } from "react-router";
import NavBar from "../NavBar";
import heroimage from "../../assets/hero-image.png";
import welcomeimage from "../../assets/welcome.png";

const WelcomeScreen = (props) => {
  const { history } = props;

  const handleNavigation = (screen) => {
    history.push(screen);
  };

  return (
    <div className="App">
      <div className="App-body">
        <img src={welcomeimage} width="600rem" />
        <img src={heroimage} width="800rem" />
        <h1>
          Stop wasting time.
          <br />
          Study smarter.
        </h1>
        <div className="my-2" />
        <div className="flex">
          <button
            class="neutral-button mx-5"
            onClick={() => handleNavigation("/login")}
          >
            Login
          </button>
          <button
            class="neutral-button mx-5"
            onClick={() => handleNavigation("/register")}
          >
            Register
          </button>
        </div>
      </div>
    </div>
  );
};

export default withRouter(WelcomeScreen);
