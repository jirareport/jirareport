alter table board
add column issue_period_name_format varchar(255);

update board
set issue_period_name_format = 'INITIAL_AND_FINAL_DATE';

alter table board
    alter column issue_period_name_format set not null;

