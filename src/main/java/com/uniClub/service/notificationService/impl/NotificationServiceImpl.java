package com.uniClub.service.notificationService.impl;

import com.uniClub.enums.StatusEnum;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.dto.notificationDto.NotificationDtoRequest;
import com.uniClub.dto.notificationDto.NotificationDtoResponse;
import com.uniClub.enums.NotificationType;
import com.uniClub.entity.notificationEntity.Notification;
import com.uniClub.mapper.notificationMapper.NotificationMapper;
import com.uniClub.repository.notificationRepository.NotificationRepository;
import com.uniClub.service.notificationService.INotificationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }


    @Override
    public NotificationDtoResponse createNotification(NotificationDtoRequest request) {
        Notification notification = notificationMapper.toNotificationEntity(request);
        String username = getUsername();
        try {
            notification.setCreatedBy(username);
            notification.setUpdatedBy(username);
            Notification saved = notificationRepository.save(notification);

            NotificationDtoResponse notificationDtoResponse = notificationMapper.toNotificationDtoResponse(saved);
            return notificationDtoResponse;
        }catch (Exception e){
            throw new BaseException(new ErrorMessage(MessageType.NOTIFICATION_CREATION_FAILED, e.getMessage()));
        }
    }

    @Override
    public List<NotificationDtoResponse> getNotificationByUsername() {
        String username= getUsername();
        List<Notification> notificationList = notificationRepository.findByCreatedBy(username);
        return notificationList.stream().map(
                notificationMapper::toNotificationDtoResponse).toList();
    }

    @Override
    public List<NotificationDtoResponse> getUnreadNotifications() {
        String username= getUsername();
        List<Notification> notificationList = notificationRepository.findByCreatedByAndIsReadFalse(username);
        return notificationList.stream().map(
                notificationMapper::toNotificationDtoResponse).toList();
    }

    @Override
    public void markAsRead(String notificationId) {
    Notification notification = notificationRepository.findById(notificationId).orElseThrow(
            () -> new BaseException(new ErrorMessage(MessageType.NOTIFICATION_NOT_FOUND, notificationId))
    );
    notification.setRead(true);
    notificationRepository.save(notification);
    }

    @Override
    public List<NotificationDtoResponse> getAllNotifications() {
        List<Notification> notificationList = notificationRepository.findAll();

        return notificationList.stream().map(
                notificationMapper::toNotificationDtoResponse
        ).toList();
    }

    @Override
    public NotificationDtoResponse getNotificationById(String notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new BaseException(new ErrorMessage(MessageType.NOTIFICATION_NOT_FOUND, notificationId))
        );
        return notificationMapper.toNotificationDtoResponse(notification);
    }

    @Override
    public List<NotificationDtoResponse> getNotificationsByType(NotificationType type) {
        return notificationRepository.findAllByType(type).stream().map(
                notificationMapper::toNotificationDtoResponse
        ).toList();
    }


    @Override
    public void deleteByUsername() {
        String username = getUsername();
        List<Notification> notifications = notificationRepository.findByCreatedBy(username);
        notifications.forEach(n -> n.setStatus(StatusEnum.Terminated));
        notificationRepository.saveAll(notifications);
    }


    @Override
    public void deleteById(String notificationId) {
    Notification notification = notificationRepository.findById(notificationId).orElseThrow(
            () -> new BaseException(new ErrorMessage(MessageType.NOTIFICATION_NOT_FOUND, notificationId))
    );
    notification.setStatus(StatusEnum.Terminated);
    notificationRepository.save(notification);
    }
    /**
     * SecurityContextHolder üzerinden oturum açan kullanıcının username bilgisini döndürür.
     * (UserDetails veya JWT fark etmeksizin güvenli şekilde çözümler.)
     */
    private String getUsername() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = auth.getPrincipal();
        return (principal instanceof org.springframework.security.core.userdetails.UserDetails user)
                ? user.getUsername()
                : principal.toString();
    }



}
