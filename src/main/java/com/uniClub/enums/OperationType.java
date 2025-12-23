package com.uniClub.enums;

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

    // 🔹 Event Operasyonları
    CREATE_EVENT("Yeni etkinlik oluşturma"),
    FIND_ALL_EVENTS("Tüm etkinlikler listelendi"),
    FIND_EVENT("Etkinlik bulma"),
    UPDATE_EVENT("Etkinlik güncelleme"),
    DELETE_EVENT("Etkinlik silme"),
    JOIN_EVENT("Etkinliğe katılma"),
    LEAVE_EVENT("Etkinlikten ayrılma"),
    APPROVE_EVENT("Etkinlik onaylama"),
    REJECT_EVENT("Etkinlik reddetme"),
    GET_UPCOMING_EVENTS_PAGED("Yaklaşan etkinlikler"),

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
    SEND_CODE("KOD GÖNDERİLDİ"),
    SEND_NOTIFICATION("Bildirim gönderme"),
    MESSAGE_SENT("Mesaj gönderme"),
    MESSAGE_RECEIVED("Mesaj alma"),


    // 🔥 🔥 🔥 KULÜP OPERASYONLARI (YENİ EKLENENLER) 🔥 🔥 🔥

    CREATE_CLUB("Yeni kulüp oluşturma"),
    FIND_CLUB("Kulüp bulma"),
    FIND_ALL_CLUBS("Tüm kulüpleri listeleme"),
    UPDATE_CLUB("Kulüp güncelleme"),
    DELETE_CLUB("Kulüp silme"),
    APPROVE_CLUB("Kulüp onaylama"),
    REJECT_CLUB("Kulüp reddetme"),
    FIND_ACTIVE_CLUB("Aktif Kulüp"),
    DEACTIVATE_CLUB("Kulüp Deactive"),
    ACTIVATE_CLUB("KULÜP AKTİF ETME"),

    // Başkan atama / kaldırma
    ASSIGN_PRESIDENT("Kulübe başkan atama"),
    REMOVE_PRESIDENT("Kulüp başkanını kaldırma"),

    // Üyelik
    ADD_MEMBER("Kulübe üye ekleme"),
    REMOVE_MEMBER("Kulüpten üye çıkarma"),
    LIST_MEMBERS("Kulüp üyelerini listeleme"),

    // Event bağlantıları
    LINK_EVENT_TO_CLUB("Etkinliği kulübe bağlama"),
    UNLINK_EVENT_FROM_CLUB("Etkinliği kulüpten kaldırma");

    private final String description;

    OperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
