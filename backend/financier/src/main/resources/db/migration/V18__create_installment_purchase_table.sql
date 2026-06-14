CREATE TABLE installment_purchase
(
    id             UUID PRIMARY KEY,

    transaction_id UUID           NOT NULL,

    total_amount   NUMERIC(19, 2) NOT NULL,
    interest_rate  NUMERIC(10, 4),

    created_at     TIMESTAMP      NOT NULL DEFAULT NOW(),
    created_by     VARCHAR(255)   NULL,
    updated_at     TIMESTAMP      NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_installment_purchase_transaction
        FOREIGN KEY (transaction_id)
            REFERENCES transaction (id)
            ON DELETE CASCADE
);