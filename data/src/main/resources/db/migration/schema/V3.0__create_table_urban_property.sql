CREATE TABLE urban_property
(
    id                          INT          NOT NULL,
    id_provider                 INT          NOT NULL,
    provider_code               VARCHAR(50)  NOT NULL,
    url                         VARCHAR(255) NOT NULL,
    contract                    VARCHAR(5)   NOT NULL,
    type                        VARCHAR(25),
    sub_type                    VARCHAR(100),
    dormitories                 INT,
    suites                      INT,
    bathrooms                   INT,
    garages                     INT,
    sell_price                  NUMERIC(14, 2),
    sell_price_variation        NUMERIC(8, 2),
    rent_price                  NUMERIC(14, 2),
    rent_price_variation        NUMERIC(8, 2),
    condominium_price           NUMERIC(14, 2),
    condominium_price_variation NUMERIC(8, 2),
    condominium_name            VARCHAR(255),
    exchangeable                VARCHAR(1),
    status                      VARCHAR(10),
    financeable                 VARCHAR(1),
    occupied                    VARCHAR(1),
    notes                       TEXT,
    creation_date               TIMESTAMP    NOT NULL,
    last_analysis_date          TIMESTAMP,
    exclusion_date              TIMESTAMP,
    analyzable                  VARCHAR(1)   NOT NULL,
    CONSTRAINT urban_property_pk
        PRIMARY KEY (id),
    CONSTRAINT urban_property_fk_provider
        FOREIGN KEY (id_provider) REFERENCES provider (id) ON DELETE CASCADE,
    CONSTRAINT urban_property_uk_provider_code
        UNIQUE (id_provider, provider_code),
    CONSTRAINT urban_property_check_contract
        CHECK ( contract IN ('SELL', 'RENT') ),
    CONSTRAINT urban_property_check_type
        CHECK ( type IN ('RESIDENTIAL', 'COMMERCIAL') ),
    CONSTRAINT urban_property_status
        CHECK ( status IN ('UNUSED', 'USED') ),
    CONSTRAINT urban_property_check_exchangeable
        CHECK ( exchangeable IN ('Y', 'N') ),
    CONSTRAINT urban_property_check_financeable
        CHECK ( financeable IN ('Y', 'N') ),
    CONSTRAINT urban_property_check_occupied
        CHECK ( occupied IN ('Y', 'N') ),
    CONSTRAINT urban_property_check_analyzable
        CHECK ( analyzable IN ('Y', 'N') )
);

CREATE SEQUENCE urban_property_seq
    AS INT;