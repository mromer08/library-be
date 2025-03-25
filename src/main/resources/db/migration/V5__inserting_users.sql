-- Crear usuarios (estudiantes y bibliotecarios)
INSERT INTO user_account (id, email, password, name, cui, birth_date, role_id, is_approved, email_verified)
VALUES
    (gen_random_uuid(), 'student1@example.com', '$2a$10$8uRZMJ6JNmWolT6.Vky9o./sMuCol51.tyNXOuTEiiH.miDo4sdH.', 'Juan Pérez', 1234567890123, '2000-01-01', (SELECT id FROM role WHERE name = 'STUDENT'), TRUE, TRUE),
    (gen_random_uuid(), 'librarian1@example.com', '$2a$10$8uRZMJ6JNmWolT6.Vky9o./sMuCol51.tyNXOuTEiiH.miDo4sdH.', 'Ana Gómez', 9876543210987, '1995-05-05', (SELECT id FROM role WHERE name = 'LIBRARIAN'), TRUE, TRUE);

-- Crear estudiantes
INSERT INTO student (id, user_id, penalty, carnet, degree_id)
VALUES
    (gen_random_uuid(), (SELECT id FROM user_account WHERE email = 'student1@example.com'), FALSE, 2023000001, (SELECT id FROM degree WHERE code = 2016000001));

-- Crear bibliotecarios
INSERT INTO librarian (id, user_id, hire_date)
VALUES
    (gen_random_uuid(), (SELECT id FROM user_account WHERE email = 'librarian1@example.com'), '2023-01-01');