alter table issue_period
    add column name varchar(255);

update issue_period
set name = '[' || TO_CHAR(start_date, 'dd/mm/yyyy') || ' - ' || TO_CHAR(end_date, 'dd/mm/yyyy') || ']';


alter table issue_period
    alter column name set not null;
