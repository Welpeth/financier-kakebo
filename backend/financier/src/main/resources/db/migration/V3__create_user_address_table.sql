CREATE TABLE user_address
(
    id_user    UUID NOT NULL,
    id_address UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by TIMESTAMP NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_address_user
        FOREIGN KEY (id_user)
            REFERENCES users (id),

    CONSTRAINT fk_user_address_address
        FOREIGN KEY (id_address)
            REFERENCES address (id),

    CONSTRAINT pk_user_address PRIMARY KEY (id_user, id_address)
);