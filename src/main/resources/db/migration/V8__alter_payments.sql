ALTER TABLE paid_fines RENAME TO payment;

ALTER TABLE payment ADD COLUMN pay_type VARCHAR(20) CHECK (pay_type IN ('NORMAL_LOAN', 'OVERDUE_LOAN', 'SANCTION')) NOT NULL;

ALTER TABLE payment
    DROP CONSTRAINT paid_fines_loan_id_key;