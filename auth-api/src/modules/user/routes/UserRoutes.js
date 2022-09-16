import { Router } from "express";
import checkToken from "../../../config/auth/checkToken.js";

import UserController  from "../controller/UserController.js";

const router = new Router();

router.post('/api/user/auth', UserController.getAccessToken);

router.use(checkToken);

router.get('/api/user/email/:email', UserController.findByEmail);

export default router;