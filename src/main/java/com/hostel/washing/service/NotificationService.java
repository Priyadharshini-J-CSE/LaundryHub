package com.hostel.washing.service;

import com.hostel.washing.model.Notification;
import com.hostel.washing.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    public void createNotification(String userId, String message, Notification.NotificationType type, String relatedId) {
        Notification notification = new Notification(userId, message, type, relatedId);
        notificationRepository.save(notification);
    }
}