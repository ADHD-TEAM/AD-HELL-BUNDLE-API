package com.adhd.ad_hell.domain.notification.query.controller;

import com.adhd.ad_hell.common.dto.Pagination;
import com.adhd.ad_hell.domain.notification.command.infrastructure.sse.NotificationSseEmitters;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationPageResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationSummaryResponse;
import com.adhd.ad_hell.domain.notification.query.service.NotificationQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationQueryController.class)
@AutoConfigureMockMvc(addFilters = false) // 테스트용 Security 필터 끄기
class NotificationQueryControllerGetUserNotificationsTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    NotificationQueryService queryService;

    // SSE 의존성도 MockBean 필요
    @MockBean
    NotificationSseEmitters emitters;

    @Test
    @DisplayName("GET /api/users/{userId}/notifications 호출 시 알림 목록과 페이징 정보가 ApiResponse 로 감싸져 반환된다")
    void getUserNotifications_success() throws Exception {
        // given
        Long userId = 100L;
        int page = 0;
        int size = 10;

        NotificationSummaryResponse n1 = NotificationSummaryResponse.builder()
                .notificationId(1L)
                .notificationTitle("알림1")
                .notificationBody("내용1")
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        NotificationSummaryResponse n2 = NotificationSummaryResponse.builder()
                .notificationId(2L)
                .notificationTitle("알림2")
                .notificationBody("내용2")
                .read(true)
                .createdAt(LocalDateTime.now())
                .build();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPages(1)
                .totalItems(2L)
                .build();

        NotificationPageResponse pageResponse = NotificationPageResponse.builder()
                .notifications(List.of(n1, n2))
                .pagination(pagination)
                .build();

        // page, size 값까지 명확히 검증하기 위해 eq(...) 사용
        given(queryService.getUserNotifications(eq(userId), eq(page), eq(size)))
                .willReturn(pageResponse);

        // when & then
        mockMvc.perform(get("/api/users/{userId}/notifications", userId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // ApiResponse<T> 의 data 안에 NotificationPageResponse 가 들어간다고 가정
                .andExpect(jsonPath("$.data.notifications[0].notificationId").value(1L))
                .andExpect(jsonPath("$.data.notifications[0].notificationTitle").value("알림1"))
                .andExpect(jsonPath("$.data.notifications[0].read").value(false))
                .andExpect(jsonPath("$.data.notifications[1].notificationId").value(2L))
                .andExpect(jsonPath("$.data.notifications[1].read").value(true))
                .andExpect(jsonPath("$.data.pagination.currentPage").value(0))
                .andExpect(jsonPath("$.data.pagination.totalPages").value(1))
                .andExpect(jsonPath("$.data.pagination.totalItems").value(2));

        // 서비스가 기대한 인자로 1번 호출됐는지도 확인
        then(queryService).should()
                .getUserNotifications(eq(userId), eq(page), eq(size));
    }

    @Test
    @DisplayName("GET /api/users/{userId}/notifications/unread-count 호출 시 미읽음 카운트가 ApiResponse 로 감싸져 반환된다")
    void getUnreadCount_success() throws Exception {
        // given
        Long userId = 200L;
        long unread = 3L;

        given(queryService.getUnreadCount(userId))
                .willReturn(unread);

        // when & then
        mockMvc.perform(get("/api/users/{userId}/notifications/unread-count", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // ApiResponse<Long> 의 data 에 숫자만 들어간다고 가정
                .andExpect(jsonPath("$.data").value((int) unread)); // long -> int 캐스팅 주의
    }
}
