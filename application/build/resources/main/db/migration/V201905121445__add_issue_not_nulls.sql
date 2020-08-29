alter table issue alter column summary set not null;

alter table issue alter column created set not null;

alter table issue alter column issue_period_id set not null;

update issue set impediment_time = 0 where impediment_time is null;
alter table issue alter column impediment_time set not null;

update issue set wait_time = 0 where wait_time is null;
alter table issue alter column wait_time set not null;

update issue set touch_time = 0 where touch_time is null;
alter table issue alter column touch_time set not null;

update issue set pct_efficiency = 0 where pct_efficiency is null;
alter table issue alter column pct_efficiency set not null;
