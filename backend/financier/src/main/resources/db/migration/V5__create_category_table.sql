CREATE TABLE category
(
    id         UUID PRIMARY KEY,
    id_journal UUID         NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by TIMESTAMP    NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_category_journal
        FOREIGN KEY (id_journal)
            REFERENCES journal (id)
);