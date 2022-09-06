INSERT INTO CATEGORY (ID, DESCRIPTION) VALUES (1000, 'Comic Books');
INSERT INTO CATEGORY (ID, DESCRIPTION) VALUES (1001, 'Movies');
INSERT INTO CATEGORY (ID, DESCRIPTION) VALUES (1002, 'Books');

INSERT INTO SUPPLIER (ID, NAME) VALUES (1000, 'Panini Comics');
INSERT INTO SUPPLIER (ID, NAME) VALUES (1001, 'Amazon');

INSERT INTO PRODUCT (ID, NAME, FK_SUPPLIER, FK_CATEGORY, QUANTITY_AVAILABLE) VALUES (1001, 'Crise nas Infinitas Terras', 1000, 1000, 10);
INSERT INTO PRODUCT (ID, NAME, FK_SUPPLIER, FK_CATEGORY, QUANTITY_AVAILABLE) VALUES (1002, 'Interestelar', 1001, 1001, 5);
INSERT INTO PRODUCT (ID, NAME, FK_SUPPLIER, FK_CATEGORY, QUANTITY_AVAILABLE) VALUES (1003, 'Harry Potter E A Pedra Filosofal', 1001, 1002, 3);