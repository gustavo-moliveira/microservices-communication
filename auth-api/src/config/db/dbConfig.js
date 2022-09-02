import Sequelize from "sequelize";

const sequelize = new Sequelize("auth-db", "admin", "root", {
    host: "localhost",
    dialect: "postgres",
    quoteIdentifier: false,
    define: {
        syncOnAssociation: true,
        timestamps: false,
        underscored: true,
        underscoreAll: true,
        freezeTableName: true 
    },
});

sequelize
.authenticate()
.then(() => {
    console.info('Connection has been stablished!');
})
.catch((err) => {
    console.error('Unable to connect to the databse.');
    console.error(err.message);
});

export default sequelize;