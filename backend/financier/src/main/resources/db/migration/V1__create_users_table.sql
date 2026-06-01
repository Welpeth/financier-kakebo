CREATE TABLE users
(
    id          UUID PRIMARY KEY,
    id_address  UUID,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    national_id VARCHAR(50),
    user_type   VARCHAR(50),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by  TIMESTAMP    NULL,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
