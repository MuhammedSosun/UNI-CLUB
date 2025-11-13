package com.uniClub.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    USER_REGISTERED,          // Yeni kullanıcı kaydı
    USER_PROFILE_UPDATED,     // Kullanıcı profilini güncelledi
    USER_ROLE_CHANGED,        // Rol veya yetki değişikliği yapıldı
    USER_REMOVED_FROM_CLUB,   // Kullanıcı kulüpten çıkarıldı

    // 🔹 CLUB MODÜLÜ
    CLUB_CREATED,             // Yeni kulüp oluşturuldu
    CLUB_UPDATED,             // Kulüp bilgileri güncellendi
    CLUB_DELETED,             // Kulüp silindi
    CLUB_MEMBER_JOINED,       // Yeni üye kulübe katıldı
    CLUB_MEMBER_LEFT,         // Üye kulüpten ayrıldı
    CLUB_PRESIDENT_CHANGED,   // Yeni kulüp başkanı atandı

    // 🔹 EVENT MODÜLÜ
    EVENT_CREATED,            // Yeni etkinlik oluşturuldu
    EVENT_UPDATED,            // Etkinlik bilgileri güncellendi
    EVENT_DELETED,            // Etkinlik iptal edildi veya silindi
    EVENT_REMINDER,           // Etkinlik yaklaşırken hatırlatma bildirimi
    EVENT_REGISTRATION_OPEN,  // Etkinliğe kayıtlar açıldı
    EVENT_REGISTRATION_CLOSED,// Etkinlik kayıtları kapandı

    // 🔹 NOTIFICATION / SYSTEM
    SYSTEM_ANNOUNCEMENT,      // Genel sistem duyurusu
    NEW_MESSAGE,              // Yeni mesaj bildirimi (örneğin chat modülü varsa)
    ERROR_ALERT,              // Hata bildirimi (admin için)
    INFO,                     // Genel bilgilendirme
    WARNING,                  // Uyarı bildirimi
    SUCCESS
}
