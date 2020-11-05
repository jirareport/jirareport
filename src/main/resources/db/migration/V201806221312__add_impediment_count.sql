alter table board add column impediment_type varchar(50);
alter table board add column impediment_columns jsonb;
alter table issue add column impediment_time bigint;