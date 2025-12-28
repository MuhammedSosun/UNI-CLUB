-- 1) Yeni BIGINT kolon ekle
ALTER TABLE password_reset_codes
    ADD COLUMN new_id BIGINT;

-- 2) UUID → BIGINT dönüşüm (hash ile güvenli cast)
UPDATE password_reset_codes
SET new_id = (abs(('x' || substr(md5(id::text), 1, 16))::bit(64)::bigint));

-- 3) Eski UUID kolonunu sil
ALTER TABLE password_reset_codes
DROP COLUMN id;

-- 4) new_id kolonunu id olarak yeniden adlandır
ALTER TABLE password_reset_codes
    RENAME COLUMN new_id TO id;

-- 5) Sequence oluştur ve id'yi otomatik arttırmalı yap
CREATE SEQUENCE password_reset_codes_id_seq OWNED BY password_reset_codes.id;

ALTER TABLE password_reset_codes
    ALTER COLUMN id SET DEFAULT nextval('password_reset_codes_id_seq');

-- 6) id kolonunu NOT NULL yap
ALTER TABLE password_reset_codes
    ALTER COLUMN id SET NOT NULL;
