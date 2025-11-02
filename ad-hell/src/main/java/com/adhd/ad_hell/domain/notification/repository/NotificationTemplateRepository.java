package com.adhd.ad_hell.domain.notification.repository;

import com.adhd.ad_hell.domain.notification.entity.NotificationTemplate;
import com.adhd.ad_hell.domain.notification.entity.enums.YnType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Page<NotificationTemplate> findByDeletedYn(YnType deletedYn, Pageable pageable);
    Page<NotificationTemplate> findByDeletedYnAndTemplateTitleContainingIgnoreCase(YnType deletedYn, String title, Pageable pageable);
}
