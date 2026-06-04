CREATE TABLE journal
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    total_value DECIMAL(15, 2),
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(255) NULL,
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);