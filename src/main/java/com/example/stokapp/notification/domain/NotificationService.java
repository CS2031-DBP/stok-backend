package com.example.stokapp.notification.domain;

import com.example.stokapp.notification.infrastructure.NotificationRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRespository notificationRepository;

    // CREAR NOTIFICACION
    public void createNotification(Notification notification) {
        notificationRepository.save(notification);
    }


}
