ALTER TABLE event_participations ADD COLUMN status VARCHAR(20);

UPDATE event_participations SET status = 'APPROVED' WHERE status IS NULL;

ALTER TABLE event_participations ALTER COLUMN status SET NOT NULL;