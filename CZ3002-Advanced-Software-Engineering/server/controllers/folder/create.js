import folderModel from '../../models/folder.js';

const createNewUser = function(userID) {

    try {

        // make a new user in folders collection with null entry
        let userFolder = new folderModel({
            _id: userID,
            filefilefileContents: null
        });

        userFolder.save(); // no need to add await
        console.log(`Done making a new folder document for user ${userID}.`);
        return null;

    } catch (error) {

        console.log(`Encountered error making a new folder document. ${error.message}`);
        return error;

    }

}

export default createNewUser;