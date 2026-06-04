CREATE TABLE holder_address
(
    id_holder   UUID         NOT NULL,
    id_address UUID         NOT NULL,
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255) NULL,
    updated_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_address_holder
        FOREIGN KEY (id_holder)
            REFERENCES holder (id),

    CONSTRAINT fk_user_address_address
        FOREIGN KEY (id_address)
            REFERENCES address (id),

    CONSTRAINT pk_holder_address PRIMARY KEY (id_holder, id_address)
);