ALTER TABLE account_card DROP COLUMN due_day;
ALTER TABLE account_card ADD COLUMN expiration_month INTEGER;
ALTER TABLE account_card ADD COLUMN expiration_year INTEGER;