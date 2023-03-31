import api from "./api";
import JSZip from "jszip";
import FileSaver from "file-saver";

const getFolders = async (userID) => {
  console.log("Getting folders...");
  return await api
    .get(`/folders/${userID}`)
    .then((res) => {
      if (res.status === 201) {
        console.log("Folder structure retrieved..");
        return res;
      } else {
        console.log("Failed to retrieve folder structure.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

const addFolder = async (userID, path, dirStructure) => {
  console.log("Adding folder...");
  return api
    .put(`/folders/add-folder/${userID}`, {
      path: path,
      dirStructure: dirStructure,
    })
    .then((res) => {
      if (res.status === 200) {
        console.log("Folder added.");
        return res;
      } else {
        console.log("Failed to add folder.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

const renameFolder = async (userID, newName, path, dirStructure) => {
  console.log("Renaming folder...");
  return api
    .put(`/folders/rename-folder/${userID}`, {
      newName: newName,
      path: path,
      dirStructure: dirStructure,
    })
    .then((res) => {
      if (res.status === 200) {
        console.log("Folder renamed.");
        return res;
      } else {
        console.log("Failed to rename folder.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

const deleteFolder = async (userID, path, dirStructure) => {
  console.log("Deleting folder...");
  return api
    .put(`/folders/delete-folder/${userID}`, {
      path: path,
      dirStructure: dirStructure,
    })
    .then((res) => {
      if (res.status === 200) {
        console.log("Folder deleted.");
        return res;
      } else {
        console.log("Failed to delete folder.");
      }
      return "ERROR";
    })
    .catch((error) => {
      console.log(error);
      return error;
    });
};

export const exportFolder = async (userID, path, dirStruc) => {
  console.log(userID);
  console.log(path);
  console.log(dirStruc);
  return api
    .get(`/flashcard/export-flashcards/${userID}`, {
      params: {
        path: path,
        dirStructure: dirStruc,
      },
    })
    .then(async (res) => {
      if (res.status === 200) {
        const { fileContents, relativePaths } = res.data;
        let zip = new JSZip();
        for (let i = 0; i < fileContents.length; i++) {
          zip.file(
            relativePaths[i] + ".txt",
            JSON.stringify(fileContents[i], 4)
          );
        }

        await zip.generateAsync({ type: "blob" }).then((zip) => {
          FileSaver.saveAs(zip, "memoise.zip");
        });

        return "SUCCESS";
      } else {
        console.log("Failed to download flashcards.");
        return "FAILURE";
      }
    })
    .catch((error) => {
      console.log(error);
      return "ERROR";
    });
};

export const upload = async (userID, path, zipFiles, dirStruc) => {

  let data = [];
  console.log(zipFiles.length)

  for (let i=0; i<zipFiles.length; i++) {
    data.push(process(zipFiles[i]));
  }

  var fileContents = [],
    relativePaths = [];

  await Promise.all(data).then((zipFilesContent) => {
    zipFilesContent.forEach((zipFileContent) => {
      zipFileContent.forEach((file) => {
        if (file.content !== "") {
          fileContents.push(JSON.parse(file.content));
          relativePaths.push(file.path);
        }
      });
    });
  });

  // update the dir structure
  return api
    .post(`/flashcard/import-flashcards/${userID}`, {
      fileContents: fileContents,
      relativePaths: relativePaths,
      dirStructure: dirStruc,
      insertPath: path,
    })
    .then(async (res) => {
      if (res.status === 201) {
        console.log("Flashcards successfully imported.");
        return res;
      } else {
        console.log("Failed to import flashcards.");
        return res;
      }
    })
    .catch((error) => {
      console.log(error);
      return "ERROR";
    });
};

async function process(zipFile) {
  return new Promise((resolve, reject) => {
    JSZip.loadAsync(zipFile)
      .then(async (zip) => {
        let zipContent = [],
          promises = [];
        zip.forEach(async (relativePath, file) => {
          const promise = file.async("string");
          promises.push(promise);
          zipContent.push({
            path: relativePath,
            content: await promise,
          });
        });
        await Promise.all(promises);
        resolve(zipContent);
      })
      .catch((error) => {
        reject(error);
      });
  });
}

export { getFolders };
export { addFolder };
export { renameFolder };
export { deleteFolder };
