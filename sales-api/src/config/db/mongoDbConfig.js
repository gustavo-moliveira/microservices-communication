import mongoose from "mongoose";

import { MONGO_DB_URL } from '../constants/secrets.js';

export function connect() {
    mongoose.connect(MONGO_DB_URL, {
        useNewUrlParser: true,
    });
    mongoose.connection.on('connected', function () {
        console.info('The application connected to MongoDB successfully.')
    });
    mongoose.connection.on('error', function () {
        console.info('Application connect error')
    });

}