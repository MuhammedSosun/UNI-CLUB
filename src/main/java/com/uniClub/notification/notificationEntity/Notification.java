package com.uniClub.entity.notificationEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uniClub.entity.baseEntity.MongoBaseEntity;
import com.uniClub.enums.StatusEnum;
import com.uniClub.enums.NotificationType;
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
    @JsonFormat(shape =  JsonFormat.Shape.STRING)
    private NotificationType type;

    private StatusEnum status;

    private boolean isRead = false;



}
