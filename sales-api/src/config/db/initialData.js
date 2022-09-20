import Order from "../../modules/sales/model/Order.js";
import {v4 as uuid4} from 'uuid';

export async function createInitialData() { 
    let existingData = await Order.find();
    if (existingData && existingData.length > 0) {
        console.info('Remove existing data...')
        await Order.collection.drop();
    }
    await Order.create({
        products: [
            {
                productId: 1001,
                quantity: 3
            },            {
                productId: 1002,
                quantity: 2
            },            {
                productId: 1003,
                quantity: 1
            },
        ],
        user: {
            id: 'asd8a7da98ad9aas0d',
            name: 'User Teste',
            email: 'usertest@gmail.com'
        },
        status: 'APPROVED',
        createdAt: new Date(),
        updatedAt: new Date(),
        transactionid: uuid4(),
        serviceid: uuid4()
    });
    await Order.create({
        products: [
            {
                productId: 1001,
                quantity: 4
            },            {
                productId: 1003,
                quantity: 2
            },
        ],
        user: {
            id: 'ad98asf9afas9fa9aaf9',
            name: 'User Teste 2',
            email: 'usertest2@gmail.com'
        },
        status: 'REJECTED',
        createdAt: new Date(),
        updatedAt: new Date(),
        transactionid: uuid4(),
        serviceid: uuid4()
    });
    let initialData = await Order.find();
    console.info(
        `Initial data was created: ${JSON.stringify(initialData, undefined, 4)}`
    );
}