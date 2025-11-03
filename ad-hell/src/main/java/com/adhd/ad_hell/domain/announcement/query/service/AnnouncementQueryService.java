package com.adhd.ad_hell.domain.announcement.query.service;

import com.adhd.ad_hell.domain.announcement.query.dto.request.AnnouncementSearchRequest;
import com.adhd.ad_hell.domain.announcement.query.dto.response.AnnouncementDetailResponse;
import com.adhd.ad_hell.domain.announcement.query.dto.response.AnnouncementSummaryResponse;
import com.adhd.ad_hell.domain.announcement.query.mapper.AnnouncementMapper;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementQueryService {

    // Mapper 의존성 주입
    // 실제 SQL 쿼리를 실행하는 객체
    private final AnnouncementMapper announcementMapper;


    // 공지사항 조회 메서드

    // 검색 조건( keyword, status, page, size)이 request 객체로 들어옴.
    public Map<String, Object> findAll(AnnouncementSearchRequest request) {

        // Mapper에서 공지사항 목록을 가져온다.
        // 각 공지사항은 AnnouncementSummaryResponse에 매핑
        List<AnnouncementSummaryResponse> list = announcementMapper.findAllAnnouncements(request);

        // 총 데이터 개수를 조회하고, 페이징 처리에 사용
        long total = announcementMapper.countAllAnnouncements(request);

        // client에게 반환할 응답 객체를 Map 형태로 생성
        Map<String, Object> result = new HashMap<>();

        // 공지사항 리스트 데이터를 content키에 넣는다
        result.put("content", list);

        // 요청에 페이지 정보가 없으면 기본값 사용한다
        result.put("page", request.pageOrDefault());
        result.put("size", request.sizeOrDefault());

        // 전체 공지사항 개수를 넣는다
        result.put("totalElements", total);

        // 총 페이지 수를 계산하고, ceil을 사용해서 나머지가 있으면 페이지를 하나 더 추가한다.
        result.put("totalPages", (int) Math.ceil((double) total / request.sizeOrDefault()));

        // Map을 반환하고 Controller에서 그대로 JSON 응답으로 보내준다.
        return result;
    }

    // 공지사항 상세 조회 메서드

    // 특정 공지사항을 ID 기준으로 상세 조회한다.
    public AnnouncementDetailResponse findById(Long id) {

        // Mapper를 통해 id에 해당하는 공지사항 상세 정보를 DB에서 조회한다.
        AnnouncementDetailResponse detail = announcementMapper.findAnnouncementDetailById(id);

        // 조회 결과가 없을 경우, 예외처리("공지사항을 찾을 수 없습니다") 메시지를 보낸다.
        if (detail == null) throw new BusinessException(ErrorCode.ANNOUNCEMENT_NOT_FOUND);

        // 조회 성공 시 AnnouncementDetailResponse DTO 반환
        return detail;
    }
}
