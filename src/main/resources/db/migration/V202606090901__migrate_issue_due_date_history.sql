INSERT INTO issue_due_date_history (issue_id, idx, created, due_date)
SELECT id                                                                                                  AS issue_id,
       (elem.ord - 1)::int                                                                                AS idx,
       CASE
           WHEN elem.value -> 'created' IS NOT NULL AND jsonb_typeof(elem.value -> 'created') = 'array'
               THEN to_timestamp(concat(
                                     elem.value -> 'created' ->> 0, '-',
                                     LPAD(elem.value -> 'created' ->> 1, 2, '0'), '-',
                                     LPAD(elem.value -> 'created' ->> 2, 2, '0'), ' ',
                                     LPAD(elem.value -> 'created' ->> 3, 2, '0'), ':',
                                     LPAD(elem.value -> 'created' ->> 4, 2, '0'), ':',
                                     COALESCE(LPAD(elem.value -> 'created' ->> 5, 2, '0'), '00')
                                 ), 'YYYY-MM-DD HH24:MI:SS')
           WHEN elem.value ->> 'created' IS NOT NULL
               THEN (elem.value ->> 'created')::timestamp
           END                                                                                            AS created,
       CASE
           WHEN elem.value -> 'dueDate' IS NOT NULL AND jsonb_typeof(elem.value -> 'dueDate') = 'array'
               THEN make_date(
                       (elem.value -> 'dueDate' ->> 0)::int,
                       (elem.value -> 'dueDate' ->> 1)::int,
                       (elem.value -> 'dueDate' ->> 2)::int
                    )
           WHEN elem.value ->> 'dueDate' IS NOT NULL
               THEN (elem.value ->> 'dueDate')::date
           END                                                                                            AS due_date
FROM issue,
     LATERAL jsonb_array_elements(due_date_history) WITH ORDINALITY AS elem(value, ord)
WHERE due_date_history IS NOT NULL;
