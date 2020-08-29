ALTER TABLE board ADD COLUMN calc_due_date BOOLEAN;
ALTER TABLE issue ADD COLUMN difference_first_and_last_due_date BIGINT;
ALTER TABLE issue ADD COLUMN due_date_history JSONB;
