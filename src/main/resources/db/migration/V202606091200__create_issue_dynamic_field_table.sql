CREATE TABLE issue_dynamic_field (
    issue_id    BIGINT       NOT NULL REFERENCES issue ON DELETE CASCADE,
    field_name  VARCHAR(255) NOT NULL,
    field_value VARCHAR(255) NOT NULL,
    PRIMARY KEY (issue_id, field_name)
);

CREATE INDEX idx_issue_dynamic_field_name_value ON issue_dynamic_field (field_name, field_value);
