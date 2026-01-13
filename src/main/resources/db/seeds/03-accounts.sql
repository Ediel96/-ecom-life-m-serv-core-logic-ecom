INSERT INTO public.accounts
(user_id, "name", account_type, balance, currency, created_at, updated_at, is_active, "type", bank_name)
VALUES
('f1abf483-8dab-4198-bea9-c3ca52e93018', 'Cuenta Débito', 'debit', 0, 'COP', TIMESTAMP '2026-01-06 03:00:40.136', TIMESTAMP '2026-01-06 03:00:40.136', TRUE, 'bank', 'Bank of Example'),
('f1abf483-8dab-4198-bea9-c3ca52e93018', 'Efectivo', 'cash', 0, 'COP', TIMESTAMP '2026-01-06 03:00:40.136', TIMESTAMP '2026-01-06 03:00:40.136', TRUE, 'cash', 'Cash');