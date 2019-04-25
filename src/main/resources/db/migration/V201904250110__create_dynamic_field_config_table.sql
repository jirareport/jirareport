CREATE TABLE dynamic_field_config (
    id                 BIGSERIAL                   NOT NULL PRIMARY KEY,
    name               VARCHAR(255)                NOT NULL,
    field              VARCHAR(255)                NOT NULL,
    board_id           BIGINT                      NOT NULL REFERENCES board ON DELETE CASCADE,
    owner              VARCHAR(255)                NOT NULL,
    last_editor        VARCHAR(255)                NOT NULL,
    created_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
