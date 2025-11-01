package com.adhd.ad_hell.domain.inquiry.command.application.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InquiryCreateRequest {

    @NotBlank
    private String title;       // 문의 제목
    @NotBlank
    private String content;     // 문의 내용
    @NotNull
    private Long userId;        // FK : User
    @NotNull
    private Long categoryId;    // FK : Category

}
