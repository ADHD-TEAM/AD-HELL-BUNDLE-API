package com.adhd.ad_hell.domain.announcement.query.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnouncementSearchRequest {

    //  검색 조건
    private Integer page;     // 요청 페이지 (null이면 기본값)
    private Integer size;     // 페이지당 항목 수 (null이면 기본값)
    private String keyword;   // 제목 + 내용 LIKE 검색용 키워드
    private String status;    // 게시 상태 (Y/N/null)

    // 기본 상수값 정의
    private static final int DEFAULT_PAGE = 1;   // 기본 페이지
    private static final int DEFAULT_SIZE = 20;  // 기본 페이지 크기
    private static final int MAX_SIZE = 100;     // 최대 페이지 크기 제한


    // ===== 페이징 계산 메서드 =====

    // 현재 페이지 번호 반환
    // null 또는 1 미만이면 기본값(1)을 사용

    public int pageOrDefault() {
        return (page == null || page < 1) ? DEFAULT_PAGE : page;
    }


    // 한 페이지에 보여줄 데이터 개수 반환
    // null 또는 1 미만이면 기본값(20)
    // 너무 크면 MAX_SIZE(100)으로 제한

    public int sizeOrDefault() {
        if (size == null || size < 1) return DEFAULT_SIZE;
        return Math.min(size, MAX_SIZE);
    }


    // SQL에서 OFFSET 계산용 메서드
    // (page - 1) * size

    public long offset() {
        return (long) (pageOrDefault() - 1) * sizeOrDefault();
    }


    // SQL LIMIT 용도
    public int limit() {
        return sizeOrDefault();
    }
}
