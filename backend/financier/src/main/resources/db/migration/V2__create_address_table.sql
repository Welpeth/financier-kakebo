CREATE TABLE address
(
    id          UUID PRIMARY KEY,
    postal_code VARCHAR(20),
    city        VARCHAR(100),
    state       VARCHAR(100),
    country     VARCHAR(100),
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(255) NULL,
    updated_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);