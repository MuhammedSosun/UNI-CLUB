package com.uniClub.notification.internal.controller;

import com.uniClub.notification.api.dto.NotificationDtoRequest;
import com.uniClub.notification.api.dto.NotificationDtoResponse;
import com.uniClub.notification.api.enums.NotificationType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface INotificationController {
    ResponseEntity<NotificationDtoResponse> createNotification(NotificationDtoRequest request);
    List<NotificationDtoResponse> getNotificationByUsername();
    List<NotificationDtoResponse> getUnreadNotifications();
    void markAsRead(String notificationId);
    List<NotificationDtoResponse> getAllNotifications();
    NotificationDtoResponse getNotificationById(String notificationId);
    List<NotificationDtoResponse> getNotificationsByType(NotificationType type);
    void deleteByUsername();
    void deleteById(String notificationId);
}
