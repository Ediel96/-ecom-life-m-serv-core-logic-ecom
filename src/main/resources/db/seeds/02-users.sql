INSERT INTO public.users
(id, "name", email, "password", username, last_name, first_name, date_of_birth, gender, "role", created_at, updated_at)
VALUES
    ('f1abf483-8dab-4198-bea9-c3ca52e93018',
     'admin main',
     'admin@email.com',
     '$2a$10$24YNoWQt1xQRdEog3xPFluqv.Q8UxzxQ5AOvPCSo9bPTdAwrnigUC',
     'admin',
     'admin',
     'admin',
     DATE '2026-01-07',
     'MALE',
     'ROLE_ADMIN',
     TIMESTAMP '2026-01-06 03:00:40.136',
     TIMESTAMP '2026-01-06 03:00:40.136');