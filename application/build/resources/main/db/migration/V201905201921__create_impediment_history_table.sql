CREATE TABLE impediment_history (
    id                 BIGSERIAL                   NOT NULL PRIMARY KEY,
    issue_id           BIGINT                      NOT NULL REFERENCES issue ON DELETE CASCADE,
    start_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    lead_time          BIGINT                      NOT NULL,
    owner              VARCHAR(255)                NOT NULL,
    last_editor        VARCHAR(255)                NOT NULL,
    created_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
