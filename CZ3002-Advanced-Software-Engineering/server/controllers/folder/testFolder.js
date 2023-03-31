import deleteFolderDoc from './delete.js';
import readFolders from './read.js';

let userID = '246';

async() =>{
    readFolders(userID);
    (folderDir) => console.log(folder.Dir);
}


//deleteFolderDoc(userID);

