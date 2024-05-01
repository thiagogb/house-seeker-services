CREATE TABLE scanner
(
    id            INT        NOT NULL,
    id_provider   INT        NOT NULL,
    creation_date TIMESTAMP  NOT NULL,
    end_date      TIMESTAMP  NOT NULL,
    status        VARCHAR(1) NOT NULL,
    error_message TEXT,
    stack_trace   TEXT,
    CONSTRAINT scanner_pk
        PRIMARY KEY (id),
    CONSTRAINT scanner_fk_provider
        FOREIGN KEY (id_provider) REFERENCES provider (id) ON DELETE CASCADE,
    CONSTRAINT scanner_status
        CHECK ( status IN ('S', 'F') )
);

CREATE SEQUENCE scanner_seq
    AS INT;