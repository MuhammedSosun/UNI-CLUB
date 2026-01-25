package com.uniClub.notification.notificationService;

import com.uniClub.notification.notificationDto.NotificationDtoRequest;
import com.uniClub.notification.notificationDto.NotificationDtoResponse;
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