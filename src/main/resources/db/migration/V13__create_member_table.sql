CREATE TYPE status_enum AS ENUM (
    'Active',
    'Suspended',
    'Terminated',
    'Private'
);


CREATE TABLE members
(
    id                 BIGSERIAL PRIMARY KEY,

    status             status_enum NOT NULL,

    user_id            UUID        NOT NULL UNIQUE,

    name               VARCHAR(255),
    surname            VARCHAR(255),
    age                INT,
    student_number     VARCHAR(50),
    faculty            VARCHAR(255),
    department         VARCHAR(255),
    level              VARCHAR(50),
    university         VARCHAR(255),

    phone              VARCHAR(50),
    profile_photo_path TEXT,
    about              TEXT,

    instagram          VARCHAR(255),
    linked_in          VARCHAR(255),
    x_account          VARCHAR(255),
    github             VARCHAR(255),
    website_url        VARCHAR(255),

    created_at         TIMESTAMP   NOT NULL,
    updated_at         TIMESTAMP,
    created_by         VARCHAR(255),
    updated_by         VARCHAR(255),
    version            INT,

    CONSTRAINT fk_member_user FOREIGN KEY (user_id)
        REFERENCES users (id)
);

CREATE TABLE member_skills (
                               member_id BIGINT NOT NULL,
                               skill VARCHAR(255),

                               FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

CREATE TABLE member_interests (
                                  member_id BIGINT NOT NULL,
                                  interest VARCHAR(255),

                                  FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);


CREATE TABLE member_certificates (
                                     member_id BIGINT NOT NULL,
                                     certificate VARCHAR(255),

                                     FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

CREATE TABLE member_languages (
                                  member_id BIGINT NOT NULL,
                                  language VARCHAR(255),

                                  FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);


CREATE TABLE member_projects (
                                 member_id BIGINT NOT NULL,
                                 project VARCHAR(255),

                                 FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);
