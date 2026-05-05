INSERT INTO tb_user (login, password, role)
VALUES ('adminlm', '$2a$12$96PKsqau7KgrFYL0I92Fount0YkVyU/FC9brG/6Q0Py2PZi60Ez66', 'ADMIN');

INSERT INTO tb_user (login, password, role)
VALUES ('visitant', '$2a$12$.0VlhncXUGvGbztgsqgShuSln3MIaKdaezeW8GGNevZAhIz5N2G2O', 'COLLABORATOR');

INSERT INTO tb_salesman (id, name, email, phone, user_id)
VALUES (1, 'Visitante Demo', 'visitant@demo.com', '11999999999', 2);

INSERT INTO tb_question (id, statement) VALUES (1, 'Como você descrevia seu disposição ao longo do dia?');

INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Sinto exaustão total desde o momento em que acordo', 4, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Começo o dia bem, mas sofro uma queda drástica após o almoço.', 8, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Sinto cansaço apenas em dias de carga de trabalho excessiva.', 15, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Mantenho disposição alta e cansaço leve apenas ao fim do dia.', 25, 1);

INSERT INTO tb_question (id, statement) VALUES (1, 'Como você descrevia a eficiência do seu planejamento diário?');

INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Passo o dia resolvendo urgências e não foco no que é estratégico.', 4, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Consigo planejar meu dia, mas as interrupções são constantes.', 8, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Tenho uma rotina organizada, com poucas falhas de cronograma.', 15, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Minha agenda é totalmente controlada e delegada com eficiência.', 25, 1);

INSERT INTO tb_question (id, statement) VALUES (1, 'Como você descrevia a seu foco e raciocínio com o passar do dia?');

INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Sinto névoa mental e dificuldade constante em tomar decisões simples.', 4, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Consigo focar, mas me distraio facilmente com e-mails e celular.', 8, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Tenho blocos de foco profundo, mas oscilo em dias estressantes.', 15, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Mantenho clareza total e foco absoluto nas metas principais.', 25, 1);

INSERT INTO tb_question (id, statement) VALUES (1, 'Como você descrevia seu autocuidado e tempo fora do trabalho?');

INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'O trabalho consome minhas noites e fins de semana; sem tempo para família/saúde.', 4, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Tento me desligar, mas estou sempre checando notificações do trabalho.', 8, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Consigo separar os horários, embora o estresse ocasionalmente interfira.', 15, 1);
INSERT INTO tb_option (id, description, weight, question_id) 
VALUES (1, 'Tenho equilíbrio total e tempo dedicado para lazer e autocuidado.', 25, 1);


INSERT INTO tb_lead (name, email, phone, salesman_id, lead_status, lead_classification, total_score) 
VALUES ('Ricardo Alta Performance', 'ricardo@diretoria.com', '16999887766', 1, 'NEW', 'HOT', 6);

INSERT INTO tb_lead (name, email, phone, salesman_id, lead_status, lead_classification, total_score) 
VALUES ('Ana Planejadora', 'ana.consultoria@outlook.com', '11988776655', 1, 'NEW', 'WARM', 45);

INSERT INTO tb_lead (name, email, phone, salesman_id, lead_status, lead_classification, total_score) 
VALUES ('Bruno Curioso', 'bruno_testes@gmail.com', '19977665544', 1, 'NEW', 'COLD', 88);

INSERT INTO tb_lead (name, email, phone, salesman_id, lead_status, lead_classification, total_score) 
VALUES ('Carla Decisora', 'carla@ceo.com.br', '11912345678', 1, 'CONTACTED', 'HOT', 4);