-- Member <-> User bire bir ilişki garantisi
-- 1 User sadece 1 Member'a sahip olabilir

ALTER TABLE members
    ADD CONSTRAINT uk_members_user_id UNIQUE (user_id);
