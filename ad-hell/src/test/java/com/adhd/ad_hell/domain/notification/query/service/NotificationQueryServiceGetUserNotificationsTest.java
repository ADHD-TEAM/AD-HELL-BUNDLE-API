package com.adhd.ad_hell.domain.notification.query.service;

import com.adhd.ad_hell.domain.notification.command.domain.aggregate.Notification;
import com.adhd.ad_hell.domain.notification.command.domain.aggregate.enums.YnType;
import com.adhd.ad_hell.domain.notification.command.infrastructure.repository.JpaNotificationRepository;
import com.adhd.ad_hell.domain.notification.command.infrastructure.repository.JpaNotificationTemplateRepository;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationPageResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationSummaryResponse;
import com.adhd.ad_hell.domain.notification.query.mapper.NotificationMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationQueryServiceGetUserNotificationsTest {

    @Mock
    JpaNotificationRepository notificationRepo;

    @Mock
    JpaNotificationTemplateRepository templateRepo;

    @Spy // 실제 mapper 로직을 사용하되, 필요하면 verify 가능
    NotificationMapper mapper = new NotificationMapper();

    @InjectMocks
    NotificationQueryService sut;

    @Test
    @DisplayName("회원 알림 목록 조회 시, userId 기준으로 페이지네이션된 NotificationPageResponse 를 반환한다")
    void getUserNotificationsSuccess() {
        // given
        Long userId = 100L;
        int page = 0;
        Integer size = 10;

        Notification n1 = Notification.builder()
                .userId(userId)
                .notificationTitle("알림1")
                .notificationBody("내용1")
                .readYn(YnType.N)
                .build();

        Notification n2 = Notification.builder()
                .userId(userId)
                .notificationTitle("알림2")
                .notificationBody("내용2")
                .readYn(YnType.Y)
                .build();

        // 페이징 결과 mock
        Page<Notification> pageResult = new PageImpl<>(
                List.of(n1, n2),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")),
                2
        );

        when(notificationRepo.findByUserId(
                eq(userId),
                any(Pageable.class)
        )).thenReturn(pageResult);

        // when
        NotificationPageResponse response = sut.getUserNotifications(userId, page, size);

        // then
        assertNotNull(response);
        assertNotNull(response.getNotifications());
        assertEquals(2, response.getNotifications().size());

        NotificationSummaryResponse first = response.getNotifications().get(0);
        assertEquals("알림1", first.getNotificationTitle());
        assertEquals("내용1", first.getNotificationBody());
        assertFalse(first.isRead());

        // pagination 확인
        assertNotNull(response.getPagination());
        assertEquals(0, response.getPagination().getCurrentPage());
        assertEquals(1, response.getPagination().getTotalPages());
        assertEquals(2, response.getPagination().getTotalItems());

        verify(notificationRepo, times(1))
                .findByUserId(eq(userId), any(Pageable.class));
        verifyNoMoreInteractions(notificationRepo);
    }

    @Test
    @DisplayName("미읽음 카운트 조회 시 YnType.N 기준 count 를 반환한다")
    void getUnreadCountSuccess() {
        // given
        Long userId = 200L;
        long unreadCount = 5L;

        when(notificationRepo.countByUserIdAndReadYn(userId, YnType.N))
                .thenReturn(unreadCount);

        // when
        long result = sut.getUnreadCount(userId);

        // then
        assertEquals(unreadCount, result);
        verify(notificationRepo, times(1))
                .countByUserIdAndReadYn(userId, YnType.N);
        verifyNoMoreInteractions(notificationRepo);
    }
}
