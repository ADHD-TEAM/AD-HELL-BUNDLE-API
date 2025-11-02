package com.adhd.ad_hell.domain.inquiry.command.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class InquiryAnswerRequest {

    @NotBlank(message = "답변 내용은 비어 있을 수 없습니다.")
    private String response;
}
