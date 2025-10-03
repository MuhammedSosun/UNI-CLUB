--SQUENCE YOK ÇÜNKÜ UUID İLE ÜRETİLMİŞ BİR MİGRATİON

CREATE TABLE users (    --gen_random_uuid otomatik id olusturmamızı sağlar
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                        --TIMESTAMP WITH TIME ZONE tarih ve saat bilgisini timezone olarakta tutmamızı sağlar örn:2025-10-03 12:00+03
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                       updated_at TIMESTAMP WITH TIME ZONE,
                       created_by VARCHAR(255),
                       updated_by VARCHAR(255),
                       version INT,

                        --HER username farklı olacak
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);
