package com.uniClub.notification.notificationMapper;

import com.uniClub.notification.notificationDto.NotificationDtoRequest;
import com.uniClub.notification.notificationDto.NotificationDtoResponse;
import com.uniClub.notification.notificationEntity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    Notification toNotificationEntity(NotificationDtoRequest  request);

    NotificationDtoResponse toNotificationDtoResponse(Notification notification);
}
