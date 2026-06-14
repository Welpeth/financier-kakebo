CREATE TABLE installment
(
    id                      UUID PRIMARY KEY,

    installment_purchase_id UUID           NOT NULL,

    installment_number      INTEGER        NOT NULL,
    amount                  NUMERIC(19, 2) NOT NULL,

    due_date                DATE           NOT NULL,
    paid                    BOOLEAN        NOT NULL DEFAULT FALSE,
    paid_at                 DATE,

    created_at              TIMESTAMP      NOT NULL DEFAULT NOW(),
    created_by              VARCHAR(255)   NULL,
    updated_at              TIMESTAMP      NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_installment_purchase
        FOREIGN KEY (installment_purchase_id)
            REFERENCES installment_purchase (id)
            ON DELETE CASCADE
);