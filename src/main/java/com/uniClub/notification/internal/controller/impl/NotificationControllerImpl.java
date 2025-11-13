package com.uniClub.notification.internal.controller.impl;

import com.uniClub.notification.api.dto.NotificationDtoRequest;
import com.uniClub.notification.api.dto.NotificationDtoResponse;
import com.uniClub.notification.api.enums.NotificationType;
import com.uniClub.notification.internal.controller.INotificationController;
import com.uniClub.notification.internal.service.INotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationControllerImpl implements INotificationController {

    private final INotificationService notificationService;

    public NotificationControllerImpl(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/create")
    public ResponseEntity<NotificationDtoResponse> createNotification(
            @RequestBody NotificationDtoRequest request) {
        return ResponseEntity.ok(notificationService.createNotification(request));
    }

    @GetMapping("/list/by/username")
    @Override
    public List<NotificationDtoResponse> getNotificationByUsername() {
        return notificationService.getNotificationByUsername();
    }
    @GetMapping("/get/unread")
    @Override
    public List<NotificationDtoResponse> getUnreadNotifications() {
        return notificationService.getUnreadNotifications();
    }
    @GetMapping("/mark")
    @Override
    public void markAsRead(String notificationId) {
        notificationService.markAsRead(notificationId);
    }
    @GetMapping("/get/all")
    @Override
    public List<NotificationDtoResponse> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
    @GetMapping("/get/{notificationId}")
    @Override
    public NotificationDtoResponse getNotificationById(@PathVariable String notificationId) {
        return notificationService.getNotificationById(notificationId);
    }
    @GetMapping("/get/type/{type}")
    @Override
    public List<NotificationDtoResponse> getNotificationsByType(@PathVariable NotificationType type) {
        return notificationService.getNotificationsByType(type);
    }
    @GetMapping("/delete")
    @Override
    public void deleteByUsername() {
        notificationService.deleteByUsername();
    }
    @GetMapping("/delete/{notificationId}")
    @Override
    public void deleteById(@PathVariable String notificationId) {
        notificationService.deleteById(notificationId);
    }
}
