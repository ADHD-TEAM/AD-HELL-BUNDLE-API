package com.adhd.ad_hell.domain.notification.query.repository;

import com.adhd.ad_hell.domain.notification.command.entity.NotificationTemplate;
import com.adhd.ad_hell.domain.notification.command.entity.YnType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateQueryRepository extends JpaRepository<NotificationTemplate, Long> {

    Page<NotificationTemplate> findByDeletedYn(YnType deletedYn, Pageable pageable);

    Page<NotificationTemplate> findByDeletedYnAndTemplateTitleContainingIgnoreCase(
            YnType deletedYn,
            String templateTitle,
            Pageable pageable
    );
}