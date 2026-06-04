CREATE TABLE "transaction"
(
    id              UUID PRIMARY KEY,
    id_account_card UUID,
    id_account      UUID           NOT NULL,
    installments    INT            DEFAULT 1,
    amount          DECIMAL(15, 2) NOT NULL,
    fee             DECIMAL(15, 2) DEFAULT 0,
    type            VARCHAR(50),
    description     VARCHAR(500),
    created_at      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(255)   NULL,
    updated_at      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_account_card
        FOREIGN KEY (id_account_card)
            REFERENCES account_card (id),

    CONSTRAINT fk_transaction_account
        FOREIGN KEY (id_account)
            REFERENCES account (id)
);