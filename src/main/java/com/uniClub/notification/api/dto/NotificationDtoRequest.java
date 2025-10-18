package com.uniClub.notification.api.dto;

import com.uniClub.notification.api.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDtoRequest {

    private String title;

    private String message;

    private NotificationType type;

    private String targetUsername;

}
