INSERT INTO board_flux_column (board_id, idx, value)
SELECT id, (elem.ord - 1)::int, elem.value
FROM board,
     LATERAL jsonb_array_elements_text(flux_column) WITH ORDINALITY AS elem(value, ord)
WHERE flux_column IS NOT NULL;

INSERT INTO board_ignore_issue_type (board_id, idx, value)
SELECT id, (elem.ord - 1)::int, elem.value
FROM board,
     LATERAL jsonb_array_elements_text(ignore_issue_type) WITH ORDINALITY AS elem(value, ord)
WHERE ignore_issue_type IS NOT NULL;

INSERT INTO board_impediment_columns (board_id, idx, value)
SELECT id, (elem.ord - 1)::int, elem.value
FROM board,
     LATERAL jsonb_array_elements_text(impediment_columns) WITH ORDINALITY AS elem(value, ord)
WHERE impediment_columns IS NOT NULL;

INSERT INTO board_touching_columns (board_id, idx, value)
SELECT id, (elem.ord - 1)::int, elem.value
FROM board,
     LATERAL jsonb_array_elements_text(touching_columns) WITH ORDINALITY AS elem(value, ord)
WHERE touching_columns IS NOT NULL;

INSERT INTO board_waiting_columns (board_id, idx, value)
SELECT id, (elem.ord - 1)::int, elem.value
FROM board,
     LATERAL jsonb_array_elements_text(waiting_columns) WITH ORDINALITY AS elem(value, ord)
WHERE waiting_columns IS NOT NULL;
