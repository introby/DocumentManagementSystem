DROP TABLE IF EXISTS audi CASCADE;

CREATE TABLE audi
(
    id                  SERIAL,
    name                varchar(100),
    creation_date       timestamp,
    count               int,
    PRIMARY KEY (id)
);

INSERT INTO audi (name, creation_date, count)
VALUES ('Audi A3', '2021-08-19 21:00:50', 19),
       ('Audi A6', '2019-10-10 14:21:01', 6),
       ('Audi Q6', '2020-03-11 08:21:00', 28),
       ('Audi TT', '2012-01-01 10:10:14', 1),
       ('Audi R8', '2019-12-31 00:00:00', 14);