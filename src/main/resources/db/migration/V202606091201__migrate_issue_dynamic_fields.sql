INSERT INTO issue_dynamic_field (issue_id, field_name, field_value)
SELECT id, kv.key, kv.value
FROM issue,
     LATERAL jsonb_each_text(dynamic_fields) AS kv(key, value)
WHERE dynamic_fields IS NOT NULL
  AND kv.value IS NOT NULL;
