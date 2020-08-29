ALTER TABLE issue_period
RENAME COLUMN avg_lead_time TO lead_time;

ALTER TABLE issue_period
RENAME COLUMN lead_time_by_size TO lead_time_by_estimate;

ALTER TABLE issue_period
RENAME COLUMN estimated TO throughput_by_estimate;

ALTER TABLE issue_period
RENAME COLUMN tasks_by_system TO throughput_by_system;

ALTER TABLE issue_period
RENAME COLUMN tasks_by_type TO throughput_by_type;

ALTER TABLE issue_period
RENAME COLUMN tasks_by_project TO throughput_by_project;

ALTER TABLE issue_period
RENAME COLUMN column_time_avgs TO column_time_avg;

ALTER TABLE issue_period
RENAME COLUMN issues_count TO throughput;
