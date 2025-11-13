package com.uniClub.dto.notificationDto;

import com.uniClub.enums.NotificationType;
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
