ALTER TABLE reservation DROP COLUMN expired;

ALTER TABLE reservation 
ADD COLUMN expiration_date DATE DEFAULT NULL;