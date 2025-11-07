package com.adhd.ad_hell.domain.ad_comment.query.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.ad_comment.query.dto.request.AdCommentSearchRequest;
import com.adhd.ad_hell.domain.ad_comment.query.dto.response.AdCommentDetailResponse;
import com.adhd.ad_hell.domain.ad_comment.query.dto.response.AdCommentListResponse;
import com.adhd.ad_hell.domain.ad_comment.query.service.AdCommentQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ad")
@Tag(name = "AdCommentQuery", description = "광고 댓글 조회 API")
public class AdCommentQueryController {

    private final AdCommentQueryService adCommentQueryService;

    @Operation(summary = "댓글 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "조회 실패")
    })
    @GetMapping("/Comment/{adCommentId}")
    public ResponseEntity<ApiResponse<AdCommentDetailResponse>> getAdComment(
            @PathVariable Long adCommentId
    ) {
        AdCommentDetailResponse response = adCommentQueryService.getComment(adCommentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "댓글 목록 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "조회 실패")
    })
    @GetMapping("/Comment")
    public ResponseEntity<ApiResponse<AdCommentListResponse>> getAdsComment(
            AdCommentSearchRequest AdCommentSearchRequest
    ) {
        AdCommentListResponse response = adCommentQueryService.getComments(AdCommentSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "내 댓글 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "조회 실패")
    })
    @GetMapping("/Comment/My")
    public ResponseEntity<ApiResponse<AdCommentListResponse>> getMyComments(
            AdCommentSearchRequest AdCommentSearchRequest
    ) {
        AdCommentListResponse response = adCommentQueryService.getMyComments(AdCommentSearchRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}
