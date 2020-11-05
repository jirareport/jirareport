CREATE TABLE column_time_average
(
    id              BIGSERIAL                   NOT NULL PRIMARY KEY,
    issue_period_id BIGINT                      NOT NULL REFERENCES issue_period ON DELETE CASCADE,
    column_name     VARCHAR(255)                NOT NULL,
    average_time    DECIMAL(10, 2)              NOT NULL,
    owner           VARCHAR(255)                NOT NULL,
    last_editor     VARCHAR(255)                NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

alter table column_time_average
    add unique (issue_period_id, column_name)
