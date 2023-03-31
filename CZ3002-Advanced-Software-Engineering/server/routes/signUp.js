import express from 'express';
import { signUp } from '../controllers/auth/signUp.js';

var router = express.Router();

router.post('/', signUp);

export default router;