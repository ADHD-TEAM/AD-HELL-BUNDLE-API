package com.adhd.ad_hell.domain.notification.command.repository;

import com.adhd.ad_hell.domain.notification.command.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
}