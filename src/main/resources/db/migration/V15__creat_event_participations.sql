DROP TABLE IF EXISTS event_participants;


CREATE TABLE event_participations
(
    id BIGSERIAL PRIMARY KEY,

    event_id  BIGINT NOT NULL,
    member_id BIGINT NOT NULL,

    attended  BOOLEAN DEFAULT FALSE,
    joined_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version    INT,

    CONSTRAINT fk_participation_event FOREIGN KEY (event_id)
        REFERENCES events(id) ON DELETE CASCADE,

    CONSTRAINT fk_participation_member FOREIGN KEY (member_id)
        REFERENCES members(id) ON DELETE CASCADE
);

