import config from '../../config.js'
import folderModel from '../../models/folder.js';
import mongoose from 'mongoose';

let CONNECTION_URL = config.mongo.uri
var PORT=5000
// mongoose.connect(CONNECTION_URL, { useNewUrlParser: true, useUnifiedTopology: true })
//   .then(() => app.listen(PORT, () => console.log(`Server running on port: ${PORT}`)))
//   .catch((error) => console.log(`${error} did not connect`));
await mongoose.connect(CONNECTION_URL);

const deleteFolderDoc = async(userID) => {

    try {

        folderModel.deleteOne({_id: userID});

        console.log(`Done deleting folder document for user ${userID}.`);
        return null;

    } catch (error) {

        console.log(`Encountered error while deleting folder document. ${error.message}`);
        return error;

    }

}

//mongoose.connection.close()

export default deleteFolderDoc;