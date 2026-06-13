ALTER TABLE transaction ADD COLUMN id_category UUID REFERENCES category(id);
