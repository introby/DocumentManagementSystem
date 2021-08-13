DROP TABLE IF EXISTS seat CASCADE;

CREATE TABLE seat
(
    id                  SERIAL,
    name                varchar(100),
    creation_date       timestamp,
    count               int,
    PRIMARY KEY (id)
);

INSERT INTO seat (name, creation_date, count)
VALUES ('Seat Leon', '2018-06-17 14:14:14', 17),
       ('Seat Ateca', '2020-07-30 08:34:00', 7),
       ('Seat Arona', '2020-02-28 17:35:00', 21),
       ('Seat Ibiza', '2018-12-12 21:14:21', 15),
       ('Seat Tarraco', '2019-02-07 09:42:15', 35);