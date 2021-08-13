DROP TABLE IF EXISTS contacts CASCADE;

CREATE TABLE contacts
(
    id        SERIAL,
    type      varchar(30),
    value     varchar(100),
    office_id bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (office_id) REFERENCES offices (id)
);

INSERT INTO contacts (type, value, office_id)
VALUES ('PHONE', '+375212120212', 1),
       ('EMAIL', 'email@email.com', 2),
       ('SITE', 'site1.com', 2),
       ('EMAIL', 'bbb@email.com', 1),
       ('PHONE', '+375175216325', 2);