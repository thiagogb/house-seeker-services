CREATE TABLE urban_property_location
(
    id                INT NOT NULL,
    id_urban_property INT NOT NULL,
    state             VARCHAR(255),
    city              VARCHAR(255),
    district          VARCHAR(255),
    zipcode           VARCHAR(8),
    street_name       VARCHAR(255),
    street_number     INT,
    complement        VARCHAR(255),
    latitude          NUMERIC(12, 9),
    longitude         NUMERIC(12, 9),
    CONSTRAINT urban_property_location_pk
        PRIMARY KEY (id),
    CONSTRAINT urban_property_location_fk_urban_property
        FOREIGN KEY (id_urban_property) REFERENCES urban_property (id) ON DELETE CASCADE
);

CREATE SEQUENCE urban_property_location_seq
    AS INT;