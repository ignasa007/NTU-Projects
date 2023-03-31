import mongoose from 'mongoose';

const userModel = mongoose.model('user', mongoose.Schema({
    refId: {
        type: String,
        unique: true
    },
    email: {
        type: String,
        required: true,
        unique: true
    },
    name: {
        type: String,
        required: true
    },
    username: String
}));

export default userModel;