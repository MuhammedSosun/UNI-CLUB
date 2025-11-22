CREATE SCHEMA IF NOT EXISTS uniclub;
CREATE TABLE IF NOT EXISTS uniclub.clubs (
                       id SERIAL PRIMARY KEY,
                       club_name VARCHAR(150) NOT NULL,
                       short_name VARCHAR(50),
                       description TEXT,
                       logo_url VARCHAR(255),
                       foundation_date DATE,
                       email VARCHAR(150),
                       phone VARCHAR(30),
                       instagram VARCHAR(150),
                       approved BOOLEAN DEFAULT FALSE,
                       president_id UUID UNIQUE,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMP,
                       created_by VARCHAR(100),
                       updated_by VARCHAR(100),
                       version INTEGER,
                       CONSTRAINT fk_club_president FOREIGN KEY (president_id)
                           REFERENCES users (id)
                           ON DELETE SET NULL
);

CREATE TABLE uniclub.club_members (
                              club_id BIGINT NOT NULL,
                              user_id UUID NOT NULL,
                              PRIMARY KEY (club_id, user_id),
                              CONSTRAINT fk_club_members_club FOREIGN KEY (club_id)
                                  REFERENCES clubs (id)
                                  ON DELETE CASCADE,
                              CONSTRAINT fk_club_members_user FOREIGN KEY (user_id)
                                  REFERENCES users (id)
                                  ON DELETE CASCADE
);

CREATE TABLE uniclub.event_clubs (
                             event_id BIGINT NOT NULL,
                             club_id BIGINT NOT NULL,
                             PRIMARY KEY (event_id, club_id),
                             CONSTRAINT fk_event_clubs_event FOREIGN KEY (event_id)
                                 REFERENCES events (id)
                                 ON DELETE CASCADE,
                             CONSTRAINT fk_event_clubs_club FOREIGN KEY (club_id)
                                 REFERENCES clubs (id)
                                 ON DELETE CASCADE
);

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'event_participants') THEN
CREATE TABLE uniclub.event_participants (
                                    event_id BIGINT NOT NULL,
                                    participant_id UUID NOT NULL,
                                    PRIMARY KEY (event_id, participant_id),
                                    CONSTRAINT fk_event_participants_event FOREIGN KEY (event_id)
                                        REFERENCES events (id)
                                        ON DELETE CASCADE
);
END IF;
END $$;
