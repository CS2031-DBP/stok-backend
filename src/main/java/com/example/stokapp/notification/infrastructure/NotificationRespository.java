package com.example.stokapp.notification.infrastructure;

import com.example.stokapp.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRespository extends JpaRepository<Notification, Long> {
}
