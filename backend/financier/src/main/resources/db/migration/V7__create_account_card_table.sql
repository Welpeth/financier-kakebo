CREATE TABLE account_card
(
    id              UUID PRIMARY KEY,
    id_account      UUID         NOT NULL,
    name            VARCHAR(255) NOT NULL,
    is_active       BOOLEAN   DEFAULT TRUE,
    card_type       VARCHAR(50),
    credit_limit    DECIMAL(15, 2),
    expiration_date TIMESTAMP,
    due_day         INT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by      TIMESTAMP    NULL,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_account_card_account
        FOREIGN KEY (id_account)
            REFERENCES account (id)
);