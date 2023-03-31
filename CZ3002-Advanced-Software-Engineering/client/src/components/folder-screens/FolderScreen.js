import { useState, useMemo, useEffect } from "react";
import React from "react";
import { withRouter } from "react-router";
import EdiText from "react-editext";
import "./Folder.scss";
import {
  addFolder,
  deleteFolder,
  getFolders,
  renameFolder,
  exportFolder,
  upload,
} from "../../services/folder-service";
import Modal from "../Modal";

function FolderScreen(props) {
  const { history } = props;

  const handleNavigation = (screen) => {
    history.push(screen);
  };

  const [showModal, setShow] = useState(false);
  const setShowModal = () => {
    setShow(true);
  };

  const setHideModal = () => {
    setShow(false);
  };

  const [showImportModal, setShowImportModal] = useState(false);

  const setHideImportModal = () => {
    setShowImportModal(false);
  };

  const [tempName, setTempName] = useState("New Folder");

  const [file, setFile] = useState();

  const [path, setPath] = useState(".");

  const [wholePath, setWholePath] = useState();

  const token = props.token;

  const [folders, setFolders] = useState([
    {
      index: 1,
      name: "New Folder",
    },
  ]);

  const [viewMode, setViewMode] = useState(true);
  function invertMode() {
    setViewMode(!viewMode);
  }

  const [dirStruc, setDirStruc] = useState();

  const [folderCount, setFolderCount] = useState(0);

  const [selectedFolder, setSelectedFolder] = useState();

  const [folderType, setFolderType] = useState();

  const [currentDirStruc, setCurrentDirStruc] = useState();

  const updateDirStruct = async () => {
    const folderData = await getFolders(token);
    try {
      if (folderData.status === 201) {
        setDirStruc(folderData.data);
        setCurrentDirStruc(folderData.data);
        return folderData.data;
      }
    } catch (error) {
      console.log(error);
    }
  };

  const handleExportFolder = async () => {
    await exportFolder(token, `${path}`, dirStruc);
  };

  const handleImportFolder = async (e) => {
    e.preventDefault();
    console.log("Importing...");
    if (file !== null) {
      let res = await upload(token, file, dirStruc);
      if (res.status === 201) {
        let folderData = res.data;
        setDirStruc(folderData);
        await handleSubFolder(`${window.location.pathname}`, folderData);
      }
    }
  };

  const handleDeleteFolder = async (toDelete) => {
    let res = await deleteFolder(token, `${path}/${toDelete.name}`, dirStruc);
    if (res.status === 200) {
      let folderData = res.data;
      setDirStruc(folderData);
      await handleSubFolder(`${window.location.pathname}`, folderData);
    }
  };

  const handleEditFolder = async (toEdit, val) => {
    let res = await renameFolder(
      token,
      val,
      `${path}/${toEdit.name}`,
      dirStruc
    );
    if (res.status === 200) {
      let folderData = res.data;
      setDirStruc(folderData);
      await handleSubFolder(`${window.location.pathname}`, folderData);
    }
  };

  const updateFolders = async (tempDirStruc) => {
    setFolders([]);
    setFolderCount(0);
    let temp = 0;
    let tempFolders = [];
    if (!tempDirStruc) {
      return;
    }
    for (let folder of Object.keys(tempDirStruc)) {
      temp = temp + 1;
      tempFolders.push({
        index: temp,
        name: folder,
      });
    }
    setFolderCount(temp);
    setFolders(tempFolders);
  };

  const handleAddFolder = async (name) => {
    let res = await addFolder(token, `${path}/${name}`, dirStruc);
    if (res.status === 200) {
      let folderData = res.data;
      setDirStruc(folderData);
      await handleSubFolder(`${window.location.pathname}`, folderData);
    }
  };

  const displayFolders = (folders) => {
    if (folderCount === 0) return;
    return folders.map((element, id) => {
      return (
        <div
          key={id}
          id="folder"
          className="flex flex-col align-middle mx-5 my-5"
        >
          {viewMode ? (
            <div>
              <button
                id="template-content"
                className={
                  element == selectedFolder
                    ? "folder selected-folder"
                    : "folder"
                }
                onClick={() =>
                  handleSubFolder(`${window.location.pathname}/${element.name}`)
                }
              >
                <h2>{element.name.slice(0, 30)}</h2>
              </button>
            </div>
          ) : (
            <div>
              <button
                id="template-content"
                className={
                  element == selectedFolder
                    ? "folder selected-folder"
                    : "folder"
                }
                onClick={() => setSelectedFolder(element)}
              <h2>{element.name.slice(0, 30)}</h2>
              </button>
              {element == selectedFolder ? (
                <div className="flex align-middle my-2">
                  <EdiText
                    type="text"
                    value={element.name}
                    onSave={(val) => handleEditFolder(element, val)}
                  />
                  <button
                    className="neutral-button my-2 mx-2"
                    onClick={() => handleDeleteFolder(element)}
                  >
                    Delete
                  </button>
                </div>
              ) : null}
            </div>
          )}
        </div>
      );
    });
  };

  const isBaseFolder = (dir) => {
    if (dir instanceof Array) {
      if (dir.length) {
        return true;
      }
    }
    return false;
  };

  const isNewFolder = (dir) => {
    if (dir instanceof Array) {
      if (!dir.length) {
        return true;
      }
    }
    if (dir === null) {
      return true;
    }
    return false;
  };

  const isSubFolder = (dir) => {
    if (dir instanceof Object) {
      return true;
    }
    return false;
  };

  useMemo(async () => {
    const tempDirStruc = await updateDirStruct();
    await handleSubFolder(window.location.pathname, tempDirStruc);
  }, []);

  useEffect(() => {
    if (isBaseFolder(currentDirStruc)) {
      console.log("im a base folder");
      history.push({
        pathname: `/home/${path}-flashcards`,
        state: { path: path, dirStruc: dirStruc, userID: token },
      });
      setFolderType("base");
    } else {
      if (isNewFolder(currentDirStruc)) {
        console.log("im a new folder");
        setFolderType("new");
      } else if (isSubFolder(currentDirStruc)) {
        console.log("im a subfolder");
        setFolderType("sub");
      }
      handleNavigation(wholePath);
    }
  }, [currentDirStruc]);

  const handleSubFolder = async (nextPath, folderData = dirStruc) => {
    let curpath = nextPath.slice(13, nextPath.length);
    curpath = "." + curpath;
    curpath = curpath.replace("%20", " ");
    console.log(curpath);
    nextPath = nextPath.replace("%20", " ");
    console.log(nextPath);
    setPath(curpath);
    setWholePath(nextPath);
    subFolder(nextPath, folderData);
  };

  const subFolder = (path, folderData) => {
    let arr = path.split("/");
    arr = arr.slice(3, arr.length);
    console.log(arr);
    console.log(folderData);
    subFolderHelper(arr, 0, folderData);
  };

  const handleBack = () => {
    console.log(wholePath);
    let filename = path.split("/");
    filename = "/" + filename[filename.length - 1];
    let prevPath = wholePath.replace(filename, "");
    handleSubFolder(prevPath, dirStruc);
    // handleNavigation(prevPath);
  };

  function subFolderHelper(arr, cur, dirStruc) {
    let value = arr.length;
    if (cur == value) {
      updateFolders(dirStruc);
      console.log("this is my updated: ", dirStruc);
      setCurrentDirStruc(dirStruc);
    } else {
      for (let folder of Object.keys(dirStruc)) {
        if (folder == arr[cur]) {
          let struc = dirStruc[folder];
          subFolderHelper(arr, cur + 1, struc);
        }
      }
    }
  }

  return (
    <body className="App-body">
      <h1>Your Folders</h1>
      Current Location: {path}
      <button className="neutral-button" onClick={handleBack}>
        Back
      </button>
      <div className="flex pt-8">
        <button
          className="warm-button mx-5"
          onClick={() => {
            setShowModal();
          }}
        >
          Add Folder
        </button>
        {folderType == "new" ? (
          <button
            className="warm-button mx-5"
            onClick={() => {
              history.push({
                pathname: `/home/${path}-flashcards`,
                state: { path: path, dirStruc: dirStruc, userID: token },
              });
            }}
          >
            Add Flashcards
          </button>
        ) : null}
        <button
          className="warm-button mx-5"
          onClick={() => {
            console.log("Exporting...");
            handleExportFolder();
          }}
        >
          Export Folder
        </button>
        <button
          className="warm-button mx-5"
          onClick={() => setShowImportModal(true)}
        >
          Import Folder
        </button>

        <Modal show={showImportModal}>
          <p>Choose ZIP file:</p>
          <br />
          <div className="form-group">
            <label htmlFor="file">File Location</label>
            <input
              type="file"
              id="file"
              name="file"
              onChange={(e) => setFile(e.target.files)}
              multiple
              className="pb-10"
            />
          </div>
          <div className="flex flex-row absolute bottom-6 right-8">
            <button
              type="button"
              className="warm-button mx-5"
              onClick={(e) => {
                handleImportFolder(e);
                setFile();
                setHideImportModal();
              }}
            >
              Create Folder
            </button>
            <button
              type="button"
              className="neutral-button"
              onClick={() => {
                setHideImportModal();
                setTempName("New Folder");
              }}
            >
              Cancel
            </button>
          </div>
        </Modal>
        {viewMode ? (
          <button className="bg-gray-100 mx-5 w-40" onClick={invertMode}>
            View Mode
          </button>
        ) : (
          <button
            className="bg-gray-100 mx-5 w-40"
            onClick={() => {
              invertMode();
              setSelectedFolder();
            }}
          >
            Edit Mode
          </button>
        )}
      </div>
      <Modal show={showModal}>
        <p>Name your new folder:</p>
        <br />
        <div className="form-group">
          <label htmlFor="name">Folder Name</label>
          <input
            type="name"
            name="name"
            id="name"
            required="required"
            placeholder="Computational Economics"
            value={tempName}
            onChange={(e) => setTempName(e.target.value)}
          />
        </div>
        <div className="flex flex-row absolute bottom-6 right-8">
          <button
            type="button"
            className="warm-button mx-5"
            onClick={() => {
              handleAddFolder(tempName);
              setHideModal();
              setTempName("New Folder");
            }}
          >
            Create Folder
          </button>
          <button
            type="button"
            className="neutral-button"
            onClick={() => {
              setHideModal();
              setTempName("New Folder");
            }}
          >
            Cancel
          </button>
        </div>
      </Modal>
      <div className="flex flex-row flex-wrap mx-20 my-5 justify-center">
        {folderCount === 0 ? "" : displayFolders(folders)}
      </div>
    </body>
  );
}

export default withRouter(FolderScreen);
