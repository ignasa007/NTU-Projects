import mongoose from 'mongoose';

const folderModel = mongoose.model('folder', mongoose.Schema({
    _id: {
        type: String,
        required: true
    },
    contents: {
        type: mongoose.Schema.Types.Mixed,
        default: {}
    }
}));

export default folderModel;