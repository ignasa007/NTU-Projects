import express from 'express';

import { login } from '../controllers/auth/login.js';
import { forgotPassword, resetPassword } from '../controllers/auth/passwordOps.js';

var router = express.Router();

router.post('/', login);
router.post('/forgot-password', forgotPassword);
router.post('/reset-password', resetPassword);

export default router;