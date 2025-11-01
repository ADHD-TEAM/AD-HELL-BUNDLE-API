package com.adhd.ad_hell.domain.notification.command.repository;

import com.adhd.ad_hell.domain.notification.command.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}