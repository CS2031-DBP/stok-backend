package com.example.stokapp.notification.Infrastructure;

import com.example.stokapp.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
