insert into column_changelog (issue_id,
                              "from",
                              "to",
                              start_date,
                              end_date,
                              lead_time,
                              owner,
                              last_editor,
                              created_at,
                              updated_at)
select issue_id,
       jsonb_extract_path_text(changelog, 'from')             as "from",
       coalesce(jsonb_extract_path_text(changelog, 'to'), '') as "to",
       to_timestamp(concat(
                            changelog -> 'created' ->> 0, '-',
                            LPAD(changelog -> 'created' ->> 1, 2, '0'), '-',
                            LPAD(changelog -> 'created' ->> 2, 2, '0'), ' ',
                            LPAD(changelog -> 'created' ->> 3, 2, '0'), ':',
                            LPAD(changelog -> 'created' ->> 4, 2, '0'), ':',
                            COALESCE(LPAD(changelog -> 'created' ->> 5, 2, '0'), '00')
                        ), 'YYYY-MM-DD HH24:MI:SS')           as start_date,
       to_timestamp(concat(
                            changelog -> 'endDate' ->> 0, '-',
                            LPAD(changelog -> 'endDate' ->> 1, 2, '0'), '-',
                            LPAD(changelog -> 'endDate' ->> 2, 2, '0'), ' ',
                            LPAD(changelog -> 'endDate' ->> 3, 2, '0'), ':',
                            LPAD(changelog -> 'endDate' ->> 4, 2, '0'), ':',
                            COALESCE(LPAD(changelog -> 'endDate' ->> 5, 2, '0'), '00')
                        ), 'YYYY-MM-DD HH24:MI:SS')           as end_date,
       jsonb_extract_path_text(changelog, 'leadTime')::bigint as lead_time,
       owner,
       owner                                                  as last_editor,
       now()                                                  as created_at,
       now()                                                  as updated_at
from (
         select id                              as issue_id,
                jsonb_array_elements(changelog) as changelog,
                owner
         from issue
     ) changelog;
