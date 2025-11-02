package com.adhd.ad_hell.domain.inquiry.query.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.common.dto.Pagination;
import com.adhd.ad_hell.domain.inquiry.query.dto.request.InquirySearchRequest;
import com.adhd.ad_hell.domain.inquiry.query.dto.response.InquiryDetailResponse;
import com.adhd.ad_hell.domain.inquiry.query.dto.response.InquirySummaryResponse;
import com.adhd.ad_hell.domain.inquiry.query.service.InquiryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class InquiryQueryController {

    private final InquiryQueryService inquiryQueryService;

    /** 회원 - 내 문의 목록 */
    @GetMapping("/my")
    public ApiResponse<?> getMyInquiries(@RequestParam Long userId,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size,
                                         @RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) String answered) {

        InquirySearchRequest req = InquirySearchRequest.builder()
                .userId(userId)
                .page(page)
                .size(size)
                .keyword(keyword)
                .answered(answered)
                .build();

        List<InquirySummaryResponse> list = inquiryQueryService.getMyInquiries(req);
        Pagination pagination = inquiryQueryService.getMyPagination(req);

        return ApiResponse.success(list, pagination);
    }

    /** 회원 - 내 문의 상세 */
    @GetMapping("/my/{id}")
    public ApiResponse<InquiryDetailResponse> getMyInquiry(@PathVariable Long id,
                                                           @RequestParam Long userId) {
        return ApiResponse.success(inquiryQueryService.getMyInquiryById(userId, id));
    }

    /** 관리자 - 전체 목록 */
    @GetMapping("/admin")
    public ApiResponse<?> getAdminInquiries(@RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer size,
                                            @RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String answered) {

        InquirySearchRequest req = InquirySearchRequest.builder()
                .page(page)
                .size(size)
                .keyword(keyword)
                .answered(answered)
                .build();

        List<InquirySummaryResponse> list = inquiryQueryService.getAdminInquiries(req);
        Pagination pagination = inquiryQueryService.getAdminPagination(req);

        return ApiResponse.success(list, pagination);
    }

    /** 관리자 - 상세 */
    @GetMapping("/admin/{id}")
    public ApiResponse<InquiryDetailResponse> getAdminInquiry(@PathVariable Long id) {
        return ApiResponse.success(inquiryQueryService.getAdminInquiryById(id));
    }
}
