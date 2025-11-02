package com.adhd.ad_hell.domain.inquiry.query.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquirySearchRequest {

    // 페이징
    private Integer page; // 요청 페이지 (1부터 시작)
    private Integer size; // 페이지당 항목 수 (기본 20)

    // 검색 조건
    private Long userId;      // 회원 ID (회원 전용)
    private Long categoryId;  // 카테고리
    private String keyword;   // 제목/내용 검색
    private String answered;  // Y / N / ALL (답변여부)

    // 정렬
    private String sortBy;     // createdAt, answeredAt 등
    private String direction;  // ASC | DESC

    // ---- 보정 메서드 ----
    public int getPage() {
        return (page == null || page < 1) ? 1 : page;
    }

    public int getSize() {
        return (size == null || size < 1) ? 20 : Math.min(size, 100);
    }

    public int getOffset() {
        return (getPage() - 1) * getSize();
    }

    public String getAnsweredSafe() {
        if (answered == null || answered.isBlank()) return "ALL";
        return switch (answered.trim().toUpperCase()) {
            case "Y", "N" -> answered.trim().toUpperCase();
            default -> "ALL";
        };
    }

    public String getSortBySafe() {
        return switch (sortBy == null ? "createdAt" : sortBy) {
            case "answeredAt", "title" -> sortBy;
            default -> "createdAt";
        };
    }

    public String getDirectionSafe() {
        return "ASC".equalsIgnoreCase(direction) ? "ASC" : "DESC";
    }
}
