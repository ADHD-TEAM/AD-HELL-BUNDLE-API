package com.adhd.ad_hell.domain.notification.command.repository;

import com.adhd.ad_hell.domain.notification.command.entity.NotificationSchedule;
import com.adhd.ad_hell.domain.notification.command.entity.NotificationScheduleStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationScheduleRepository extends JpaRepository<NotificationSchedule, Long> {
    List<NotificationSchedule> findByScheduleStatusAndScheduledAtLessThanEqual(
            NotificationScheduleStatus scheduleStatus,
            LocalDateTime scheduledAt
    );
}