ALTER TABLE configuration
ADD COLUMN max_loans INT NOT NULL DEFAULT 3,
ADD COLUMN loan_period_days INT NOT NULL DEFAULT 3,
ADD COLUMN loan_overdue_limit INT NOT NULL DEFAULT 30;

UPDATE configuration
SET max_loans = 3, loan_period_days = 3, loan_overdue_limit = 30;
