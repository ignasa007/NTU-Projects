import mongoose from 'mongoose';
import folderModel from '../../../models/folder.js';
import { sleep } from '../../utils.js';
import readFolders from '../read.js';
import config from '../../../config.js';

// await mongoose.connect(JSON.parse(process.env.mongo).uri);
await mongoose.connect(config.mongo.uri);

let deleted = folderModel.collection.drop()
    .then((data) => {
        console.log('Successfully dropped collection folders.');
        return;
    })
await Promise.resolve(deleted);

let read = readFolders('246')
    .then((data) => {
        console.log(`Read user data - ${data}`);
        return;
    })
    .catch((error) => {
        console.log(error.message);
    });
await Promise.resolve(read);

await sleep(5000);
mongoose.connection.close();