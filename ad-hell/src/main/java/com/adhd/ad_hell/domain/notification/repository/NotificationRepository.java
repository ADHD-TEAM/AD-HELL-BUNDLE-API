package com.adhd.ad_hell.domain.notification.repository;

import com.adhd.ad_hell.domain.notification.entity.Notification;
import com.adhd.ad_hell.domain.notification.entity.enums.YnType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    long countByUserIdAndReadYn(Long userId, YnType readYn);
}
