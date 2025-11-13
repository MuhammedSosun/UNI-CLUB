
CREATE TABLE IF NOT EXISTS event_participants (
                                                  event_id BIGINT NOT NULL,
                                                  participant_id UUID NOT NULL,
                                                  CONSTRAINT fk_event_participants_event
                                                  FOREIGN KEY (event_id) REFERENCES events (id)
    ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_event_participant
    ON event_participants (event_id, participant_id);
