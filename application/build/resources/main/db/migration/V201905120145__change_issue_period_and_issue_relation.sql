delete
from issue i
where not exists(
        select 1
        from issue_period_issue ipi
        where ipi.issue_id = i.id
    );

alter table issue add column issue_period_id bigint;

update issue as i
set issue_period_id = ipi.issue_period_id
from issue_period_issue as ipi
where ipi.issue_id =  i.id;

drop table issue_period_issue;

alter table issue
   add constraint fk_issue_period
   foreign key (issue_period_id)
   references issue_period(id)
   on delete cascade;

