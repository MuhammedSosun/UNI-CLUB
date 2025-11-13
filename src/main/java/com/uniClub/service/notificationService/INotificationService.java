package com.uniClub.service.notificationService;

import com.uniClub.dto.notificationDto.NotificationDtoRequest;
import com.uniClub.dto.notificationDto.NotificationDtoResponse;
import com.uniClub.enums.NotificationType;

import java.util.List;

public interface INotificationService {
    NotificationDtoResponse createNotification(NotificationDtoRequest request);
    List<NotificationDtoResponse> getNotificationByUsername();
    List<NotificationDtoResponse> getUnreadNotifications();
    void markAsRead(String notificationId);
    List<NotificationDtoResponse> getAllNotifications();
    NotificationDtoResponse getNotificationById(String notificationId);
    List<NotificationDtoResponse> getNotificationsByType(NotificationType type);
    void deleteByUsername();
    void deleteById(String notificationId);

}