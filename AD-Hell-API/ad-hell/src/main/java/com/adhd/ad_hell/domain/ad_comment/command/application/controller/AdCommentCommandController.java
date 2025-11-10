package com.adhd.ad_hell.domain.ad_comment.command.application.controller;

import com.adhd.ad_hell.domain.ad_comment.command.application.dto.request.AdCommentCreateRequest;
import com.adhd.ad_hell.domain.ad_comment.command.application.dto.request.AdCommentUpdateRequest;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.ad_comment.command.application.service.AdCommentCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// ... existing code ...

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Tag(name = "AdCommentQuery", description = "광고 댓글 조회 API")
public class AdCommentCommandController {



    private final AdCommentCommandService adCommentCommandService;
    @Operation(summary = "댓글 등록")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    /* 광고 댓글 등록 */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createAdComment(
            @RequestBody AdCommentCreateRequest req
    ) {
        adCommentCommandService.createAdComment(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "댓글 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })
    /* 광고 댓글 수정 */
    @PutMapping("/{adCommentId}")
    public ResponseEntity<ApiResponse<Void>> updateAdComment(
            @PathVariable Long adCommentId,
            @RequestBody AdCommentUpdateRequest req
    ) {
        adCommentCommandService.updateAdComment(adCommentId, req);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "댓글 수정")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "실패")
    })

    /* 광고 댓글 삭제 */
    @DeleteMapping("/{adCommentId}")
    public ResponseEntity<ApiResponse<Void>> deleteAdComment(
            @PathVariable Long adCommentId
    ) {
        adCommentCommandService.deleteAdComment(adCommentId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
