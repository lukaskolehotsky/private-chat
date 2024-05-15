CREATE TABLE IF NOT EXISTS message (
    id uuid DEFAULT gen_random_uuid(),
    sender uuid NOT NULL,
    receiver uuid NOT NULL,
    message VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    displayed BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_tbl (
    id uuid DEFAULT gen_random_uuid(),
    username VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
    );