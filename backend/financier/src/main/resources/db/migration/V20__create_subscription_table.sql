CREATE TABLE subscription
(
    id               UUID PRIMARY KEY,

    transaction_id   UUID         NOT NULL,

    frequency        VARCHAR(20)  NOT NULL,
    next_charge_date DATE         NOT NULL,
    active           BOOLEAN      NOT NULL DEFAULT TRUE,

    created_at       TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by       VARCHAR(255) NULL,
    updated_at       TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_subscription_transaction
        FOREIGN KEY (transaction_id)
            REFERENCES transaction (id)
            ON DELETE CASCADE
);