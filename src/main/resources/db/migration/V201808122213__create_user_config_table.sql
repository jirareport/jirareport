CREATE TABLE user_config (
    id                 BIGSERIAL                   NOT NULL PRIMARY KEY,
    username           VARCHAR(255)                NOT NULL,
    state              VARCHAR(255),
    city               VARCHAR(255),
    holiday_token      VARCHAR(255),
    owner              VARCHAR(255)                NOT NULL,
    last_editor        VARCHAR(255)                NOT NULL,
    created_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
