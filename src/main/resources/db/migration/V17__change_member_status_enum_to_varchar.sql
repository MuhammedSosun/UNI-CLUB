-- =========================================
-- CHANGE members.status FROM ENUM TO VARCHAR
-- =========================================

-- 1) Enum değerlerini stringe çevir
ALTER TABLE members
ALTER COLUMN status TYPE VARCHAR(50)
USING status::text;

-- 2) Eğer artık kullanılmıyorsa enum type'ı sil
DROP TYPE IF EXISTS uniclub.status_enum;
