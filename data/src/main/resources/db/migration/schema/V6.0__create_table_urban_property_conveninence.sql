CREATE TABLE urban_property_convenience
(
    id                INT          NOT NULL,
    id_urban_property INT          NOT NULL,
    description       VARCHAR(255) NOT NULL,
    CONSTRAINT urban_property_convenience_pk
        PRIMARY KEY (id),
    CONSTRAINT urban_property_convenience_fk_urban_property
        FOREIGN KEY (id_urban_property) REFERENCES urban_property (id) ON DELETE CASCADE
);

CREATE SEQUENCE urban_property_convenience_seq
    AS INT;