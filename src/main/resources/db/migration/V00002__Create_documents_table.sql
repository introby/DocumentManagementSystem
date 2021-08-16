CREATE TABLE documents (
    document_id SERIAL,
    document_name varchar(200) NOT NULL,
    created_at date NOT NULL,
    supplier varchar(200) NOT NULL,
    consumer varchar(200) NOT NULL,
    contract_term date NOT NULL,
    status varchar(30),
    description varchar(2048),
    price real NOT NULL,
    deleted boolean NOT NULL,
    PRIMARY KEY (document_id)
);

INSERT INTO documents (document_name, created_at, supplier, consumer, contract_term, status, description, price, deleted)
VALUES ('doc1', '2021-06-11', 'supplier', 'consumer', '2021-07-01', 'NEW', 'description', 15, 'false');