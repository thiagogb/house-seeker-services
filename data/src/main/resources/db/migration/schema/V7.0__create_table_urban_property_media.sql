CREATE TABLE urban_property_media
(
    id                INT           NOT NULL,
    id_urban_property INT           NOT NULL,
    link              VARCHAR(1000) NOT NULL,
    link_thumb        VARCHAR(1000),
    media_type        VARCHAR(5)    NOT NULL,
    extension         VARCHAR(10)   NOT NULL,
    CONSTRAINT urban_property_media_pk
        PRIMARY KEY (id),
    CONSTRAINT urban_property_media_fk_urban_property
        FOREIGN KEY (id_urban_property) REFERENCES urban_property (id) ON DELETE CASCADE,
    CONSTRAINT urban_property_media_check_media_type
        CHECK ( media_type IN ('IMAGE', 'VIDEO', 'AUDIO') )
);

CREATE SEQUENCE urban_property_media_seq
    AS INT;