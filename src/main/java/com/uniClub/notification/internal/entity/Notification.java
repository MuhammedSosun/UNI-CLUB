package com.uniClub.notification.internal.entity;

import com.uniClub.common.baseEntity.MongoBaseEntity;
import com.uniClub.notification.api.enums.NotificationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications")
public class Notification extends MongoBaseEntity {

    @Id
    private String id;

    private String title;

    private String message;


    private NotificationType notificationType;

    private boolean isRead = false;

    private String targetUsername;

}
