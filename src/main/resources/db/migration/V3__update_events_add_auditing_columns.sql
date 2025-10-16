-- Gereksiz sütunu kaldır
ALTER TABLE uniclub.events
DROP COLUMN IF EXISTS created_by_user;

-- BaseEntity auditing sütunlarını ekle
ALTER TABLE uniclub.events
    ADD COLUMN IF NOT EXISTS created_by VARCHAR(255),
    ADD COLUMN IF NOT EXISTS updated_by VARCHAR(255);