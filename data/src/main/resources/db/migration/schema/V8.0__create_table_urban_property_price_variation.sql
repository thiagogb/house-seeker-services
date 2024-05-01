CREATE TABLE urban_property_price_variation
(
    id                INT         NOT NULL,
    id_urban_property INT         NOT NULL,
    analysis_date     TIMESTAMP   NOT NULL,
    type              VARCHAR(15) NOT NULL,
    price             NUMERIC(14, 2),
    variation         NUMERIC(8, 2),
    CONSTRAINT urban_property_price_variation_pk
        PRIMARY KEY (id),
    CONSTRAINT urban_property_price_variation_fk_urban_property
        FOREIGN KEY (id_urban_property) REFERENCES urban_property (id) ON DELETE CASCADE,
    CONSTRAINT urban_property_price_variation_type
        CHECK ( type IN ('SELL', 'RENT', 'CONDOMINIUM') )
);

CREATE SEQUENCE urban_property_price_variation_seq
    AS INT;