package com.example.stokapp.notification.domain;


import com.example.stokapp.notification.Infrastructure.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationSercice {

    @Autowired
    private NotificationRepository notificationRepository;

    // CREAR NOTIFICACION
    public void createNotification(Notification notification) {
        notificationRepository.save(notification);
    }


}
