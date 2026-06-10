CREATE TABLE board_flux_column (
    board_id BIGINT       NOT NULL REFERENCES board ON DELETE CASCADE,
    idx      INTEGER      NOT NULL,
    value    VARCHAR(255) NOT NULL,
    PRIMARY KEY (board_id, idx)
);

CREATE TABLE board_ignore_issue_type (
    board_id BIGINT       NOT NULL REFERENCES board ON DELETE CASCADE,
    idx      INTEGER      NOT NULL,
    value    VARCHAR(255) NOT NULL,
    PRIMARY KEY (board_id, idx)
);

CREATE TABLE board_impediment_columns (
    board_id BIGINT       NOT NULL REFERENCES board ON DELETE CASCADE,
    idx      INTEGER      NOT NULL,
    value    VARCHAR(255) NOT NULL,
    PRIMARY KEY (board_id, idx)
);

CREATE TABLE board_touching_columns (
    board_id BIGINT       NOT NULL REFERENCES board ON DELETE CASCADE,
    idx      INTEGER      NOT NULL,
    value    VARCHAR(255) NOT NULL,
    PRIMARY KEY (board_id, idx)
);

CREATE TABLE board_waiting_columns (
    board_id BIGINT       NOT NULL REFERENCES board ON DELETE CASCADE,
    idx      INTEGER      NOT NULL,
    value    VARCHAR(255) NOT NULL,
    PRIMARY KEY (board_id, idx)
);
