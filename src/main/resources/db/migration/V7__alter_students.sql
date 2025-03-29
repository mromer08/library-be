ALTER TABLE student 
    RENAME COLUMN penalty TO is_sanctioned;

ALTER TABLE student 
    ADD COLUMN debt NUMERIC(10,2) DEFAULT 0 NOT NULL;
