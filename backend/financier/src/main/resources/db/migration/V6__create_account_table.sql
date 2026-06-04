CREATE TABLE account
(
    id           UUID PRIMARY KEY,
    id_holder      UUID         NOT NULL,
    name         VARCHAR(255) NOT NULL,
    is_active    BOOLEAN        DEFAULT TRUE,
    balance      DECIMAL(15, 2) DEFAULT 0,
    account_type VARCHAR(50),
    created_at   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    created_by   VARCHAR(255)   NULL,
    updated_at   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_account_holder
        FOREIGN KEY (id_holder)
            REFERENCES holder (id)
);