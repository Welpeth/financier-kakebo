CREATE TABLE notification
(
    id           UUID PRIMARY KEY,

    id_holder    UUID         NOT NULL,

    type         VARCHAR(30)  NOT NULL,
    reference_id UUID         NOT NULL,
    title        VARCHAR(255),
    message      VARCHAR(500),
    amount       NUMERIC(19, 2),
    due_date     DATE         NOT NULL,
    is_read      BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by   VARCHAR(255) NULL,
    updated_at   TIMESTAMP    NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_notification_holder
        FOREIGN KEY (id_holder)
            REFERENCES holder (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_notification_holder ON notification (id_holder);

-- dedup guard: one notification per source occurrence
CREATE UNIQUE INDEX uq_notification_ref_due ON notification (reference_id, due_date);
