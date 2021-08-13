DROP TABLE IF EXISTS vw CASCADE;

CREATE TABLE vw
(
    id                  SERIAL,
    name                varchar(100),
    creation_date       timestamp,
    count               int,
    PRIMARY KEY (id)
);

INSERT INTO vw (name, creation_date, count)
VALUES ('VW Golf', '2004-10-19 10:23:54', 1),
       ('VW Polo', '2012-02-10 17:34:00', 4),
       ('VW Jetta', '2021-07-24 14:00:00', 2),
       ('VW Boro', '2020-01-20 00:00:00', 1),
       ('VW Passat', '2019-12-31 09:14:15', 7);