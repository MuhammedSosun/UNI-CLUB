CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- USERS TABLOSU
CREATE TABLE IF NOT EXISTS uniclub.users (
                                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version INT,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
    );

-- REFRESH TOKEN TABLOSU
CREATE TABLE IF NOT EXISTS uniclub.refresh_token (
                                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version INT,

    refresh_token TEXT NOT NULL UNIQUE,
    expired_date TIMESTAMPTZ NOT NULL,
    user_id UUID NOT NULL,

    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id)
    REFERENCES uniclub.users(id)
    ON DELETE CASCADE
    );

-- İNDEKSLER
CREATE INDEX IF NOT EXISTS idx_user_username ON uniclub.users (username);
CREATE INDEX IF NOT EXISTS idx_refresh_token_user_id ON uniclub.refresh_token (user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_token_expired_date ON uniclub.refresh_token (expired_date);
