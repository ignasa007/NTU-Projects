import { withRouter } from "react-router";

const ProtectedNavBar = (props) => {
  const { history } = props;

  const handleNavigation = (screen) => {
    history.push(screen);
  };

  const logout = () => {
    localStorage.removeItem("token");
    handleNavigation("/");
    console.log("Logout");
  };

  return (
    <div className="App-header">
      <div className="flex flex-row justify-between">
        <a href="/home/folders" className="text-black hover:text-white">
          Memoise
        </a>
        <div className="flex flex-row">
          <a href="/home/folders">
            <button className="neutral-button">Folders</button>
          </a>
          <button className="neutral-button ml-4" onClick={logout}>
            Log out
          </button>
        </div>
      </div>
    </div>
  );
};

export default withRouter(ProtectedNavBar);
