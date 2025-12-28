CREATE TABLE IF NOT EXISTS password_reset_codes (
                                                    id UUID PRIMARY KEY,
                                                    email VARCHAR(255) NOT NULL,
    code VARCHAR(10) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE
    );
