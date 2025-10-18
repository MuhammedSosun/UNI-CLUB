package com.uniClub.notification.internal.repository;

import com.uniClub.notification.internal.entity.Notification;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification,String> {
    @Mappings({
            @Mapping(target = "id", ignore = true), // Mongo ID otomatik üretilir
            @Mapping(target = "isRead", constant = "false"), // default olarak okunmamış
            @Mapping(source = "type", target = "notificationType")
    })
    @Mapping(source = "notificationType", target = "type")
    List<Notification> findByTargetUsername(String targetUsername);
}
