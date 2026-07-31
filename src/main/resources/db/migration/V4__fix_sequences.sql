SELECT setval('tb_user_id_seq', COALESCE((SELECT MAX(id) FROM tb_user), 1));
SELECT setval('tb_salesman_id_seq', COALESCE((SELECT MAX(id) FROM tb_salesman), 1));
SELECT setval('tb_question_id_seq', COALESCE((SELECT MAX(id) FROM tb_question), 1));
SELECT setval('tb_option_id_seq', COALESCE((SELECT MAX(id) FROM tb_option), 1));
SELECT setval('tb_lead_id_seq', COALESCE((SELECT MAX(id) FROM tb_lead), 1));
