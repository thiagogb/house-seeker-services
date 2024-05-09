CREATE TABLE provider
(
    id              INT          NOT NULL,
    name            VARCHAR(255) NOT NULL,
    site_url        VARCHAR(255) NOT NULL,
    data_url        VARCHAR(255),
    mechanism       VARCHAR(50)  NOT NULL,
    params          TEXT,
    cron_expression VARCHAR(255),
    logo            bytea,
    active          VARCHAR(1)   NOT NULL,
    CONSTRAINT provider_pk
        PRIMARY KEY (id),
    CONSTRAINT provider_uk_name
        UNIQUE (name),
    CONSTRAINT provider_check_mechanism
        CHECK ( mechanism IN ('UNIVERSAL_SOFTWARE', 'JETIMOB_V1', 'JETIMOB_V2', 'JETIMOB_V3', 'JETIMOB_V4', 'SUPER_LOGICA', 'ALAN_WGT') ),
    CONSTRAINT provider_check_active
        CHECK (active IN ('Y', 'N'))
);

CREATE SEQUENCE provider_seq
    AS INT;