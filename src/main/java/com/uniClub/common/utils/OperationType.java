package com.uniClub.common.utils;

public enum OperationType {

    // 🔹 Genel CRUD Operasyonları
    CREATE("Yeni kayıt oluşturma"),
    READ("Veri görüntüleme"),
    UPDATE("Veri güncelleme"),
    DELETE("Veri silme"),

    // 🔹 Authentication & Authorization
    LOGIN("Kullanıcı girişi"),
    LOGOUT("Kullanıcı çıkışı"),
    REGISTER("Yeni kullanıcı kaydı"),
    REFRESH_TOKEN("Token yenileme"),
    PASSWORD_RESET("Şifre sıfırlama"),
    ACCOUNT_LOCKED("Hesap kilitlendi"),
    ACCOUNT_UNLOCKED("Hesap kilidi kaldırıldı"),

    // 🔹 Business Domain Operasyonları (Kulüp sistemi için)
    CREATE_EVENT("Yeni etkinlik oluşturma"),
    FIND_ALL_EVENTS("Tüm Etkinlikler listelendi"),
    FIND_EVENT("Etkinlik bulma"),
    UPDATE_EVENT("Etkinlik güncelleme"),
    DELETE_EVENT("Etkinlik silme"),
    JOIN_EVENT("Etkinliğe katılma"),
    LEAVE_EVENT("Etkinlikten ayrılma"),
    APPROVE_EVENT("Etkinliği onaylama"),
    REJECT_EVENT("Etkinliği reddetme"),

    // 🔹 Kullanıcı İşlemleri
    UPDATE_PROFILE("Profil güncelleme"),
    CHANGE_ROLE("Kullanıcı rolü değiştirme"),
    UPLOAD_DOCUMENT("Belge yükleme"),
    DELETE_DOCUMENT("Belge silme"),

    // 🔹 Admin / System
    SYSTEM_STARTUP("Sistem başlatıldı"),
    SYSTEM_SHUTDOWN("Sistem kapatıldı"),
    SCHEDULED_JOB("Zamanlanmış görev çalıştı"),
    DATA_EXPORT("Veri dışa aktarımı"),
    DATA_IMPORT("Veri içe aktarımı"),

    // 🔹 İletişim / Bildirim
    SEND_EMAIL("E-posta gönderme"),
    SEND_NOTIFICATION("Bildirim gönderme"),
    MESSAGE_SENT("Mesaj gönderme"),
    MESSAGE_RECEIVED("Mesaj alma");

    private final String description;

    OperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
