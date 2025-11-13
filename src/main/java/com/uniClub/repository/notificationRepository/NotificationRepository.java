package com.uniClub.repository.notificationRepository;

import com.uniClub.enums.NotificationType;
import com.uniClub.entity.notificationEntity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    // Kullanıcının hedef kullanıcı adına göre bildirimleri getirir
    List<Notification> findByCreatedBy(String targetUsername);

    // Belirli tipteki bildirimleri getir (örnek)
    List<Notification> findAllByType(NotificationType notificationType);

    List<Notification> findByCreatedByAndIsReadFalse(String username);

}
