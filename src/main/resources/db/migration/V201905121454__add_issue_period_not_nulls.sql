update issue_period set jql = '' where jql is null;
alter table issue_period alter column jql set not null;

update issue_period set wip_avg = 0 where wip_avg is null;
alter table issue_period alter column wip_avg set not null;

update issue_period set avg_pct_efficiency = 0 where avg_pct_efficiency is null;
alter table issue_period alter column avg_pct_efficiency set not null;
