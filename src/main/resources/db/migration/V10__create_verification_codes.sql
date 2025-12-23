CREATE TABLE verification_codes (
                                    id BIGSERIAL PRIMARY KEY,
                                    email VARCHAR(255) NOT NULL,
                                    code VARCHAR(6) NOT NULL,
                                    expire_at TIMESTAMP NOT NULL
);
