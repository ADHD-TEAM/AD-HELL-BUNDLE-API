package com.adhd.ad_hell.domain.inquiry.query.service;

import com.adhd.ad_hell.common.dto.Pagination;
import com.adhd.ad_hell.domain.inquiry.query.dto.request.InquirySearchRequest;
import com.adhd.ad_hell.domain.inquiry.query.dto.response.InquiryDetailResponse;
import com.adhd.ad_hell.domain.inquiry.query.dto.response.InquirySummaryResponse;
import com.adhd.ad_hell.domain.inquiry.query.mapper.InquiryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor

// InquiryQueryService (필터/정렬/페이징 계산) + Mapper 호출 기능 담당
public class InquiryQueryService {

    private final InquiryMapper inquiryMapper;

    // 회원 - 내 문의 목록 조회
    // 회원이 요청시 본인 글 전체를 조회하고 기본적으로 20개씩 페이징 됨.
    // 검색 조건이 들어오면 필터링으로 검색 시작.
    public List<InquirySummaryResponse> getMyInquiries(InquirySearchRequest req) {
        return inquiryMapper.findMyInquiries(req);
    }

    // 회원 - 내 문의 목록 페이징 정보 계산
    // DB에 Count 쿼리를 날려서 조건에 맞는 데이터가 몇 개인지 구하는 것.(총 게시글 수)를 구함
    // 전체 아이템 수 / 한페이지에 보여줄 개수로 나눠 총 페이지 수 계산.
    public Pagination getMyPagination(InquirySearchRequest req) {
        long totalItems = inquiryMapper.countMyInquiries(req);
        int totalPages = (int) Math.ceil((double) totalItems / req.getSize());

        // 공통 Pagination DTO
        return Pagination.builder()
                .currentPage(req.getPage())
                .totalPages(totalPages)
                .totalItems(totalItems)
                .build();
    }

    // 회원 - 내 문의 상세 조회
    // 본인 글만 조회되도록 UserId + id를 함께 전달 함
    public InquiryDetailResponse getMyInquiryById(Long userId, Long id) {
        return inquiryMapper.findMyInquiryById(userId, id);
    }

    // 관리자 - 전체 문의 목록 조회 ( 검색 / 필터 / 정렬은 통일 규칙)
    public List<InquirySummaryResponse> getAdminInquiries(InquirySearchRequest req) {
        return inquiryMapper.findAdminInquiries(req);
    }

    // 관리자 - 전체 문의 목록 페이징 정보
    public Pagination getAdminPagination(InquirySearchRequest req) {
        long totalItems = inquiryMapper.countAdminInquiries(req);
        int totalPages = (int) Math.ceil((double) totalItems / req.getSize());
        return Pagination.builder()
                .currentPage(req.getPage())
                .totalPages(totalPages)
                .totalItems(totalItems)
                .build();
    }
    // 관리자 - 문의 상세 조회
    // 단건 조회 - 관리자니까 userId 제한 없이 id로만 조회
    public InquiryDetailResponse getAdminInquiryById(Long id) {
        return inquiryMapper.findAdminInquiryById(id);
    }
}