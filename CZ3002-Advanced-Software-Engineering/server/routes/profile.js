import express from 'express';

import { changePassword } from '../controllers/auth/passwordOps.js';

var router = express.Router();

router.post('/change-password', changePassword);

export default router;