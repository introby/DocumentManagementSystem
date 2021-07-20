CREATE TABLE offices
(
    id                SERIAL,
    name              varchar(255) NOT NULL,
    city              varchar(255) NOT NULL,
    working_time_from time,
    working_time_to   time,
    metadata          text,
    PRIMARY KEY (id)
);

INSERT INTO offices (name, city, working_time_from, working_time_to, metadata)
VALUES ('Office 1', 'Vitebsk', '08:00', '18:00', ''),
       ('Office 2', 'Minsk', '09:00', '19:00', 'metadata');