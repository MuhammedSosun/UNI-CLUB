package com.uniClub.mapper.notificationMapper;

import com.uniClub.dto.notificationDto.NotificationDtoRequest;
import com.uniClub.dto.notificationDto.NotificationDtoResponse;
import com.uniClub.entity.notificationEntity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    Notification toNotificationEntity(NotificationDtoRequest  request);

    NotificationDtoResponse toNotificationDtoResponse(Notification notification);
}
