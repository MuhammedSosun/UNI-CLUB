package com.uniClub.notification.internal.mapper;

import com.uniClub.notification.api.dto.NotificationDtoRequest;
import com.uniClub.notification.api.dto.NotificationDtoResponse;
import com.uniClub.notification.internal.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    Notification toNotificationEntity(NotificationDtoRequest  request);

    NotificationDtoResponse toNotificationDtoResponse(Notification notification);
}
