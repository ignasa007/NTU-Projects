const NavBar = (props) => {
  const { history } = props;

  const handleNavigation = (screen) => {
    history.push(screen);
  };

  return (
    <div className="App-header">
      <div className="flex flex-row justify-between">
        <a href="/" className="text-black hover:text-white">
          Memoise
        </a>
        <div className="flex flex-row">
          <a href="/login">
            <button className="neutral-button">Login</button>
          </a>
          <a href="/register">
            <button className="neutral-button ml-4">Register</button>
          </a>
        </div>
      </div>
    </div>
  );
};

export default NavBar;
