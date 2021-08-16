CREATE TABLE audi
(
    id                  SERIAL,
    auto_name           varchar(100),
    creation_date       varchar(30),
    count               int,
    PRIMARY KEY (id)
);

INSERT INTO audi (auto_name, creation_date, count)
VALUES ('Audi A3', '2021-08-19', 19),
       ('Audi A6', '2019-10-10', 6),
       ('Audi Q6', '2020-03-11', 28),
       ('Audi TT', '2012-01-01', 1),
       ('Audi R8', '2019-12-31', 14);