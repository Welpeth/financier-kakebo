CREATE TABLE account
(
    id           UUID PRIMARY KEY,
    id_user      UUID         NOT NULL,
    name         VARCHAR(255) NOT NULL,
    is_active    BOOLEAN        DEFAULT TRUE,
    balance      DECIMAL(15, 2) DEFAULT 0,
    account_type VARCHAR(50),
    created_at   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    created_by   TIMESTAMP    NULL,
    updated_at   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_account_user
        FOREIGN KEY (id_user)
            REFERENCES users (id)
);