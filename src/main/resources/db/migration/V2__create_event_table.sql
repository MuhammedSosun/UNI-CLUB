CREATE TABLE IF NOT EXISTS uniclub.events (
                                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_date TIMESTAMP WITH TIME ZONE NOT NULL,
                             location VARCHAR(255),
    created_by UUID REFERENCES uniclub.users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE,
                             version INT
                             );
