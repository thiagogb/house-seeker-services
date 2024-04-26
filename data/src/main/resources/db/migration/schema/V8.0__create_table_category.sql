CREATE TABLE category
(
    id   INT          NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT category_pk
        PRIMARY KEY (id)
);

CREATE SEQUENCE category_seq
    AS INT;