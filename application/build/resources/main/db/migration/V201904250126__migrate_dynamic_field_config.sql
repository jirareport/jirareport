insert into dynamic_field_config (name, field, board_id, owner, last_editor, created_at, updated_at)
select jsonb_extract_path_text(dynamic_field, 'name')  as name,
       jsonb_extract_path_text(dynamic_field, 'field') as field,
       board_id,
       owner,
       owner                                           as last_editor,
       now()                                           as created_at,
       now()                                           as updated_at
from (
         select id                                   as board_id,
                owner                                as owner,
                jsonb_array_elements(dynamic_fields) as dynamic_field
         from board
     ) dynamic_fields;
