package com.uniClub.notification.internal.repository;

import com.uniClub.notification.api.enums.NotificationType;
import com.uniClub.notification.internal.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    // Kullanıcının hedef kullanıcı adına göre bildirimleri getirir
    List<Notification> findByCreatedBy(String targetUsername);

    // Belirli tipteki bildirimleri getir (örnek)
    List<Notification> findAllByType(NotificationType notificationType);

    List<Notification> findByCreatedByAndIsReadFalse(String username);

}
