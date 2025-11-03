package com.adhd.ad_hell.domain.notification.query.service;

import com.adhd.ad_hell.domain.notification.command.domain.aggregate.NotificationTemplate;
import com.adhd.ad_hell.domain.notification.command.domain.aggregate.enums.NotificationTemplateKind;
import com.adhd.ad_hell.domain.notification.command.domain.aggregate.enums.YnType;
import com.adhd.ad_hell.domain.notification.command.infrastructure.repository.JpaNotificationRepository;
import com.adhd.ad_hell.domain.notification.command.infrastructure.repository.JpaNotificationTemplateRepository;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationTemplatePageResponse;
import com.adhd.ad_hell.domain.notification.query.dto.response.NotificationTemplateSummaryResponse;
import com.adhd.ad_hell.domain.notification.query.mapper.NotificationMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationQueryServiceGetTemplatesTest {

    @Mock
    JpaNotificationRepository notificationRepo;   // 사용 안 해도 Mock 필요

    @Mock
    JpaNotificationTemplateRepository templateRepo;

    @Mock
    NotificationMapper mapper;

    @InjectMocks
    NotificationQueryService sut;   // System Under Test

    @DisplayName("검색어 없으면 deletedN 조건으로 10개 페이징 조회한다")
    @Test
    void noKeyWordTest() {
        // given
        String keyword = null;
        int page = 0;
        Integer size = null; // DEFAULT_PAGE_SIZE(10) 적용 기대

        // Page 요청 정보 (service 내부에서 만들어지는 것과 동일한 스펙)
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // 페이징 결과 더미 데이터
        NotificationTemplate t1 = NotificationTemplate.builder()
                .templateKind(NotificationTemplateKind.NORMAL)
                .templateTitle("공지1")
                .templateBody("내용1")
                .deletedYn(YnType.no())
                .build();

        Page<NotificationTemplate> pageResult =
                new PageImpl<>(List.of(t1), pageable, 1);

        // repo, mapper 스텁 설정
        when(templateRepo.findByDeletedYn(eq(YnType.no()), any(Pageable.class)))
                .thenReturn(pageResult);

        NotificationTemplatePageResponse mappedResponse =
                NotificationTemplatePageResponse.builder()
                        .templates(List.of(
                                NotificationTemplateSummaryResponse.builder()
                                        .templateId(1L)
                                        .templateKind(NotificationTemplateKind.NORMAL)
                                        .templateTitle("공지1")
                                        .templateBody("내용1")
                                        .createdAt(LocalDateTime.now())
                                        .build()
                        ))
                        .pagination(
                                com.adhd.ad_hell.common.dto.Pagination.builder()
                                        .currentPage(0)
                                        .totalPages(1)
                                        .totalItems(1L)
                                        .build()
                        )
                        .build();

        when(mapper.toTemplatePage(pageResult))
                .thenReturn(mappedResponse);

        // when
        NotificationTemplatePageResponse result =
                sut.getTemplates(keyword, page, size);

        // then
        // 1) 리포지토리 호출 검증 + Pageable 검증
        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);

        verify(templateRepo, times(1))
                .findByDeletedYn(eq(YnType.no()), pageableCaptor.capture());
        verify(templateRepo, never())
                .findByDeletedYnAndTemplateTitleContainingIgnoreCase(any(), anyString(), any());

        Pageable used = pageableCaptor.getValue();
        assertEquals(0, used.getPageNumber());
        assertEquals(10, used.getPageSize());
        assertTrue(used.getSort().getOrderFor("createdAt").isDescending());

        // 2) mapper 호출 검증
        verify(mapper, times(1)).toTemplatePage(pageResult);

        // 3) 최종 응답 검증
        assertNotNull(result);
        assertEquals(mappedResponse, result);  // 같은 객체여야 함
        assertEquals(1, result.getTemplates().size());
        assertEquals("공지1", result.getTemplates().get(0).getTemplateTitle());
    }

    @DisplayName("검색어가 있으면 title 검색 쿼리가 호출된다")
    @Test
    void KeyWordTest() {
        // given
        String keyword = "이벤트";
        int page = 0;
        Integer size = 10;   // 명시적으로 10

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        NotificationTemplate t1 = NotificationTemplate.builder()
                .templateKind(NotificationTemplateKind.EVENT)
                .templateTitle("이벤트 공지")
                .templateBody("이벤트 내용")
                .deletedYn(YnType.no())
                .build();

        Page<NotificationTemplate> pageResult =
                new PageImpl<>(List.of(t1), pageable, 1);

        when(templateRepo.findByDeletedYnAndTemplateTitleContainingIgnoreCase(
                eq(YnType.no()), eq(keyword), any(Pageable.class))
        ).thenReturn(pageResult);

        NotificationTemplatePageResponse mappedResponse =
                NotificationTemplatePageResponse.builder()
                        .templates(List.of(
                                NotificationTemplateSummaryResponse.builder()
                                        .templateId(2L)
                                        .templateKind(NotificationTemplateKind.EVENT)
                                        .templateTitle("이벤트 공지")
                                        .templateBody("이벤트 내용")
                                        .createdAt(LocalDateTime.now())
                                        .build()
                        ))
                        .pagination(
                                com.adhd.ad_hell.common.dto.Pagination.builder()
                                        .currentPage(0)
                                        .totalPages(1)
                                        .totalItems(1L)
                                        .build()
                        )
                        .build();

        when(mapper.toTemplatePage(pageResult))
                .thenReturn(mappedResponse);

        // when
        NotificationTemplatePageResponse result =
                sut.getTemplates(keyword, page, size);

        // then
        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);

        verify(templateRepo, times(1))
                .findByDeletedYnAndTemplateTitleContainingIgnoreCase(
                        eq(YnType.no()),
                        eq(keyword),
                        pageableCaptor.capture()
                );
        verify(templateRepo, never())
                .findByDeletedYn(any(), any());

        Pageable used = pageableCaptor.getValue();
        assertEquals(0, used.getPageNumber());
        assertEquals(10, used.getPageSize());
        assertTrue(used.getSort().getOrderFor("createdAt").isDescending());

        verify(mapper, times(1)).toTemplatePage(pageResult);

        assertNotNull(result);
        assertEquals(mappedResponse, result);
        assertEquals(1, result.getTemplates().size());
        assertEquals("이벤트 공지", result.getTemplates().get(0).getTemplateTitle());
    }
}
