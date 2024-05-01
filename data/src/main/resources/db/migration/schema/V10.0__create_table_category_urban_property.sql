CREATE TABLE category_urban_property
(
    id                INT NOT NULL,
    id_category       INT NOT NULL,
    id_urban_property INT NOT NULL,
    show_order        INT NOT NULL,
    CONSTRAINT category_urban_property_pk
        PRIMARY KEY (id),
    CONSTRAINT category_urban_property_fk_category
        FOREIGN KEY (id_category) REFERENCES category (id) ON DELETE CASCADE,
    CONSTRAINT category_urban_property_fk_urban_property
        FOREIGN KEY (id_urban_property) REFERENCES urban_property (id) ON DELETE CASCADE,
    CONSTRAINT category_urban_property_uk_1
        UNIQUE (id_category, id_urban_property),
    CONSTRAINT category_urban_property_uk_2
        UNIQUE (id_category, show_order)
);

CREATE SEQUENCE category_urban_property_seq
    AS INT;