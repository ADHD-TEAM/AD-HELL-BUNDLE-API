package com.adhd.ad_hell.domain.notification.repository;

import com.adhd.ad_hell.domain.notification.entity.NotificationSchedule;
import com.adhd.ad_hell.domain.notification.entity.enums.NotificationScheduleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationScheduleRepository extends JpaRepository<NotificationSchedule, Long> {
    List<NotificationSchedule> findByScheduleStatusAndScheduledAtLessThanEqual(
            NotificationScheduleStatus scheduleStatus, LocalDateTime scheduledAt);
}
