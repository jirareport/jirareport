CREATE TABLE issue_due_date_history
(
    issue_id BIGINT  NOT NULL REFERENCES issue ON DELETE CASCADE,
    idx      INTEGER NOT NULL,
    created  TIMESTAMP WITHOUT TIME ZONE,
    due_date DATE,
    PRIMARY KEY (issue_id, idx)
);
