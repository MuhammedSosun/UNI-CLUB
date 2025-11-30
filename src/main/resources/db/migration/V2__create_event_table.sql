
CREATE TABLE IF NOT EXISTS uniclub.events (
                                              id BIGSERIAL PRIMARY KEY,
                                              title VARCHAR(255) NOT NULL,
    description TEXT,
    event_date TIMESTAMP WITH TIME ZONE,
                             location VARCHAR(255),
    created_by_user VARCHAR(255), -- sadece username saklanıyor
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE,
                             version INT
                             );

CREATE INDEX IF NOT EXISTS idx_events_created_by_user ON uniclub.events (created_by_user);
CREATE INDEX IF NOT EXISTS idx_events_event_date ON uniclub.events (event_date);
