ALTER TABLE session
    ADD COLUMN code      VARCHAR(4) NULL;
ALTER TABLE attendances
    ADD COLUMN try_count INT NULL;
