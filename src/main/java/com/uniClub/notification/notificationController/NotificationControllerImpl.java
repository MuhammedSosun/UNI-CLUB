package com.uniClub.notification.notificationController;

import com.uniClub.notification.notificationDto.NotificationDtoRequest;
import com.uniClub.notification.notificationDto.NotificationDtoResponse;
import com.uniClub.enums.NotificationType;
import com.uniClub.notification.notificationService.INotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationControllerImpl {

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
   
    public List<NotificationDtoResponse> getNotificationByUsername() {
        return notificationService.getNotificationByUsername();
    }
    @GetMapping("/get/unread")
   
    public List<NotificationDtoResponse> getUnreadNotifications() {
        return notificationService.getUnreadNotifications();
    }
    @GetMapping("/mark")
   
    public void markAsRead(String notificationId) {
        notificationService.markAsRead(notificationId);
    }
    @GetMapping("/get/all")
   
    public List<NotificationDtoResponse> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
    @GetMapping("/get/{notificationId}")
   
    public NotificationDtoResponse getNotificationById(@PathVariable String notificationId) {
        return notificationService.getNotificationById(notificationId);
    }
    @GetMapping("/get/type/{type}")
   
    public List<NotificationDtoResponse> getNotificationsByType(@PathVariable NotificationType type) {
        return notificationService.getNotificationsByType(type);
    }
    @GetMapping("/delete")
   
    public void deleteByUsername() {
        notificationService.deleteByUsername();
    }
    @GetMapping("/delete/{notificationId}")
   
    public void deleteById(@PathVariable String notificationId) {
        notificationService.deleteById(notificationId);
    }
}
