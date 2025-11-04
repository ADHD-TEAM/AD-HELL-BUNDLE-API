// src/test/java/com/adhd/ad_hell/domain/notification/command/application/controller/NotificationCommandControllerCreateTemplateTest.java
package com.adhd.ad_hell.domain.notification.command.application.controller;

import com.adhd.ad_hell.domain.notification.command.application.dto.request.NotificationTemplateCreateRequest;
import com.adhd.ad_hell.domain.notification.command.application.dto.response.NotificationTemplateResponse;
import com.adhd.ad_hell.domain.notification.command.application.service.NotificationCommandService;
import com.adhd.ad_hell.domain.notification.command.domain.aggregate.enums.NotificationTemplateKind;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationCommandController.class)
@AutoConfigureMockMvc(addFilters = false)   // Security 필터 끔
class NotificationCommandControllerCreateTemplateTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean   // @Mock → @MockBean 으로 변경
    NotificationCommandService commandService;

    @DisplayName("공지알림 템플릿 등록API가 201과 응답바디를 반환한다")
    @Test
    void NotificationTemplateResponseTest() throws Exception {
        // given: service 가 반환할 mock response 세팅
        NotificationTemplateResponse serviceRes = NotificationTemplateResponse.builder()
                .templateId(1L)
                .templateKind(NotificationTemplateKind.NORMAL)
                .templateTitle("공지 제목")
                .templateBody("공지 내용입니다.")
                .build();

        when(commandService.createTemplate(any(NotificationTemplateCreateRequest.class)))
                .thenReturn(serviceRes);

        // when & then: HTTP 요청 보내고 검증
        mockMvc.perform(post("/api/admin/notifications/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "templateKind": "NORMAL",
                                  "templateTitle": "공지 제목",
                                  "templateBody": "공지 내용입니다."
                                }
                                """))
                .andExpect(status().isCreated())
                // ApiResponse<T> 구조라고 가정 -> {"success":true, "data":{...}} 같은 형식일 것
                .andExpect(jsonPath("$.data.templateId").value(1L))
                .andExpect(jsonPath("$.data.templateKind").value("NORMAL"))
                .andExpect(jsonPath("$.data.templateTitle").value("공지 제목"))
                .andExpect(jsonPath("$.data.templateBody").value("공지 내용입니다."));
    }
}
