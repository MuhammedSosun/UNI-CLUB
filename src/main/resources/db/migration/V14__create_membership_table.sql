DROP TABLE IF EXISTS club_members;


CREATE TYPE club_role_enum AS ENUM (
    'PRESIDENT',
    'VICE_PRESIDENT',
    'BOARD_MEMBER',
    'STANDARD_MEMBER'
);

CREATE TABLE club_memberships
(
    id BIGSERIAL PRIMARY KEY,

    club_id   BIGINT  NOT NULL,
    member_id BIGINT  NOT NULL,

    role club_role_enum,
    approved BOOLEAN DEFAULT FALSE,

    joined_at DATE,
    left_at   DATE,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version    INT,

    CONSTRAINT fk_membership_club FOREIGN KEY (club_id)
        REFERENCES clubs (id) ON DELETE CASCADE,

    CONSTRAINT fk_membership_member FOREIGN KEY (member_id)
        REFERENCES members (id) ON DELETE CASCADE
);

