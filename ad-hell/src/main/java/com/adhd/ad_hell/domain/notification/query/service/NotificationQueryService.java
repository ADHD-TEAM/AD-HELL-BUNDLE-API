package com.adhd.ad_hell.domain.notification.query.service;

import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationPageResponse;
import com.adhd.ad_hell.domain.notification.query.repository.NotificationQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService {

    private static final int DEFAULT_PAGE_SIZE = 10;

    private final NotificationQueryRepository notificationQueryRepository;

    public NotificationPageResponse getUserNotifications(Long userId, int page, Integer size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), resolveSize(size), Sort.by(Sort.Direction.DESC, "createdAt"));
        return NotificationPageResponse.from(notificationQueryRepository.findByUserId(userId, pageable));
    }

    private int resolveSize(Integer size) {
        if (size == null || size <= 0) {
            return DEFAULT_PAGE_SIZE;
        }
        return size;
    }
}