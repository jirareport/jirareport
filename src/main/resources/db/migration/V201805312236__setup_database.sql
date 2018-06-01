CREATE TABLE public.board (
    id                 BIGSERIAL                   NOT NULL PRIMARY KEY,
    name               VARCHAR(255)                NOT NULL,
    external_id        BIGINT                      NOT NULL,
    ignore_issue_type  JSONB,
    start_column       VARCHAR(255),
    end_column         VARCHAR(255),
    flux_column        JSONB,
    systemcf           VARCHAR(255),
    epiccf             VARCHAR(255),
    projectcf          VARCHAR(255),
    estimatecf         VARCHAR(255),
    owner              VARCHAR(255)                NOT NULL,
    last_editor        VARCHAR(255)                NOT NULL,
    created_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at         TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.holiday (
    id           SERIAL                       NOT NULL PRIMARY KEY,
    board_id     BIGINT                       NOT NULL REFERENCES public.board ON DELETE CASCADE,
    date         DATE                         NOT NULL,
    description  VARCHAR(255)                 NOT NULL,
    owner        VARCHAR(255)                 NOT NULL,
    last_editor  VARCHAR(255)                 NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE  NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE  NOT NULL
);

CREATE TABLE public.lead_time_config (
    id           BIGSERIAL                   NOT NULL PRIMARY KEY,
    board_id     BIGINT                      NOT NULL REFERENCES public.board ON DELETE CASCADE,
    name         VARCHAR(255)                NOT NULL,
    start_column VARCHAR(255)                NOT NULL,
    end_column   VARCHAR(255)                NOT NULL,
    owner        VARCHAR(255)                NOT NULL,
    last_editor  VARCHAR(255)                NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.issue (
    id           BIGSERIAL                   NOT NULL PRIMARY KEY,
    board_id     BIGINT                      NOT NULL REFERENCES public.board ON DELETE CASCADE,
    key          VARCHAR(255)                NOT NULL,
    summary      VARCHAR(255),
    issue_type   VARCHAR(255),
    start_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    lead_time    BIGINT                      NOT NULL,
    epic         VARCHAR(255),
    estimated    VARCHAR(255),
    project      VARCHAR(255),
    system       VARCHAR(255),
    created      TIMESTAMP WITHOUT TIME ZONE,
    creator      VARCHAR(255),
    changelog    JSONB,
    owner        VARCHAR(255)                NOT NULL,
    last_editor  VARCHAR(255)                NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.issue_period (
    id                      BIGSERIAL                   NOT NULL PRIMARY KEY,
    board_id                BIGINT                      NOT NULL REFERENCES public.board ON DELETE CASCADE,
    start_date              DATE                        NOT NULL,
    end_date                DATE                        NOT NULL,
    avg_lead_time           DECIMAL(10, 2)              NOT NULL,
    issues_count            INTEGER                     NOT NULL,
    column_time_avgs        JSONB,
    estimated               JSONB,
    histogram               JSONB,
    lead_time_by_project    JSONB,
    lead_time_by_size       JSONB,
    lead_time_by_system     JSONB,
    lead_time_by_type       JSONB,
    lead_time_compare_chart JSONB,
    tasks_by_project        JSONB,
    tasks_by_system         JSONB,
    tasks_by_type           JSONB,
    owner                   VARCHAR(255)                NOT NULL,
    last_editor             VARCHAR(255)                NOT NULL,
    created_at              TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at              TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE public.issue_period_issue (
    issue_period_id BIGINT NOT NULL REFERENCES public.issue_period ON DELETE CASCADE,
    issue_id        BIGINT NOT NULL REFERENCES public.issue ON DELETE CASCADE
);

CREATE TABLE public.lead_time (
    id                  BIGSERIAL                    NOT NULL,
    issue_id            BIGINT                       NOT NULL REFERENCES public.issue ON DELETE CASCADE,
    lead_time_config_id BIGINT                       NOT NULL REFERENCES public.lead_time_config ON DELETE CASCADE,
    lead_time           BIGINT,
    end_date            TIMESTAMP WITHOUT TIME ZONE,
    start_date          TIMESTAMP WITHOUT TIME ZONE,
    owner               VARCHAR(255)                 NOT NULL,
    last_editor         VARCHAR(255)                 NOT NULL,
    created_at          TIMESTAMP WITHOUT TIME ZONE  NOT NULL,
    updated_at          TIMESTAMP WITHOUT TIME ZONE  NOT NULL
);
