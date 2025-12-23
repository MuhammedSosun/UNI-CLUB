DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM pg_type
    WHERE typname = 'club_membership_status_enum'
  ) THEN
CREATE TYPE club_membership_status_enum AS ENUM (
      'PENDING',
      'APPROVED',
      'REJECTED',
      'LEFT'
    );
END IF;
END $$;

ALTER TABLE club_memberships
    ADD COLUMN IF NOT EXISTS status club_membership_status_enum NOT NULL DEFAULT 'PENDING';

ALTER TABLE club_memberships
DROP COLUMN IF EXISTS approved;

ALTER TABLE club_memberships
    ADD COLUMN IF NOT EXISTS requested_at DATE;

ALTER TABLE club_memberships
    ADD CONSTRAINT uk_club_memberships_club_member UNIQUE (club_id, member_id);

CREATE INDEX IF NOT EXISTS idx_club_memberships_club_status
    ON club_memberships (club_id, status);
