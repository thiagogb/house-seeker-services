CREATE TABLE urban_property_measure
(
    id                 INT NOT NULL,
    id_urban_property  INT NOT NULL,
    total_area         NUMERIC(11, 2),
    private_area       NUMERIC(11, 2),
    usable_area        NUMERIC(11, 2),
    terrain_total_area NUMERIC(11, 2),
    terrain_front      NUMERIC(11, 2),
    terrain_back       NUMERIC(11, 2),
    terrain_left       NUMERIC(11, 2),
    terrain_right      NUMERIC(11, 2),
    area_unit          VARCHAR(2),
    CONSTRAINT urban_property_measure_pk
        PRIMARY KEY (id),
    CONSTRAINT urban_property_measure_fk_urban_property
        FOREIGN KEY (id_urban_property) REFERENCES urban_property (id) ON DELETE CASCADE
);

CREATE SEQUENCE urban_property_measure_seq
    AS INT;