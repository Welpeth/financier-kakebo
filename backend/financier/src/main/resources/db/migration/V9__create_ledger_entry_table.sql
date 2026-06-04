CREATE TABLE ledger_entry
(
    id             UUID         PRIMARY KEY,
    id_journal     UUID         NOT NULL,
    id_transaction UUID         NOT NULL,
    name           VARCHAR(255),
    is_active      BOOLEAN      DEFAULT TRUE,
    final_date     TIMESTAMP,
    created_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(255) NULL,
    updated_at     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ledger_entry_journal
        FOREIGN KEY (id_journal)
            REFERENCES journal (id),

    CONSTRAINT fk_ledger_entry_transaction
        FOREIGN KEY (id_transaction)
            REFERENCES "transaction" (id)
);