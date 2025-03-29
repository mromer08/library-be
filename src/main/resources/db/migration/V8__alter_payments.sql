-- Renombrar la tabla paid_fines a payment
ALTER TABLE paid_fines RENAME TO payment;

ALTER TABLE payment ADD COLUMN pay_type VARCHAR(20) CHECK (type IN ('NORMAL_LOAN', 'OVERDUE_LOAN', 'SANCTION')) NOT NULL;
