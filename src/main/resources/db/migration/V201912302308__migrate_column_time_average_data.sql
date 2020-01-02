insert into column_time_average (issue_period_id, column_name, average_time, owner, last_editor, created_at, updated_at)
select id                                                                                          as issue_period_id,
       jsonb_extract_path_text(jsonb_array_elements(column_time_avg), 'columnName')                as column_name,
       jsonb_extract_path_text(jsonb_array_elements(column_time_avg), 'avgTime')::double precision as avg_time,
       owner                                                                                       as owner,
       owner                                                                                       as last_editor,
       now()                                                                                       as created_at,
       now()                                                                                       as updated_at
from issue_period;
