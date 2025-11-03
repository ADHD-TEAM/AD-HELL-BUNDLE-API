package com.adhd.ad_hell.domain.announcement.query.controller;

import com.adhd.ad_hell.domain.announcement.query.dto.request.AnnouncementSearchRequest;
import com.adhd.ad_hell.domain.announcement.query.dto.response.AnnouncementDetailResponse;
import com.adhd.ad_hell.domain.announcement.query.service.AnnouncementQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
@RequiredArgsConstructor
public class AnnouncementQueryController {

    // Service 의존성 주입 (조회 로직 담당)
    private final AnnouncementQueryService announcementQueryService;

    // 공지사항 목록 조회

    @GetMapping
    public ResponseEntity<Map<String, Object>> findAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {

        // 요청 파라미터를 DTO(검색 조건 객체)로 변환
        AnnouncementSearchRequest req = AnnouncementSearchRequest.builder()
                .page(page)
                .size(size)
                .keyword(keyword)
                .status(status)
                .build();

        // Http 200 OK로 응답
        return ResponseEntity.ok(announcementQueryService.findAll(req));
    }

    // 공지사항 목록 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDetailResponse> findById(@PathVariable Long id) {

        // 존재하지 않을 경우 BusinessException 던짐
        return ResponseEntity.ok(announcementQueryService.findById(id));
    }
}
