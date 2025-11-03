package com.adhd.ad_hell.domain.board_comment.query.service;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.common.dto.Pagination;
import com.adhd.ad_hell.domain.board_comment.query.dto.request.BoardCommentSearchRequest;
import com.adhd.ad_hell.domain.board_comment.query.dto.response.BoardCommentDetailResponse;
import com.adhd.ad_hell.domain.board_comment.query.dto.response.BoardCommentSummaryResponse;
import com.adhd.ad_hell.domain.board_comment.query.mapper.BoardCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardCommentQueryService {

    private final BoardCommentMapper boardCommentMapper;

    /** 게시판 내 댓글 목록 */
    public ApiResponse<List<BoardCommentSummaryResponse>> findAllBoardComments(BoardCommentSearchRequest req) {
        int page = req.getPage() != null ? req.getPage() : 1;
        int size = req.getSize() != null ? req.getSize() : 20;

        req = BoardCommentSearchRequest.builder()
                .page(page)
                .size(size)
                .boardId(req.getBoardId())
                .keyword(req.getKeyword())
                .build();

        List<BoardCommentSummaryResponse> comments = boardCommentMapper.findAllBoardComments(req);
        long total = boardCommentMapper.countComments(req);

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPages((int) Math.ceil((double) total / size))
                .totalItems(total)
                .build();

        return ApiResponse.success(comments, pagination);
    }

    /** 내 댓글 조회 */
    public ApiResponse<List<BoardCommentSummaryResponse>> findMyComments(BoardCommentSearchRequest req) {
        int page = req.getPage() != null ? req.getPage() : 1;
        int size = req.getSize() != null ? req.getSize() : 20;

        req = BoardCommentSearchRequest.builder()
                .page(page)
                .size(size)
                .writerId(req.getWriterId())
                .build();

        List<BoardCommentSummaryResponse> comments = boardCommentMapper.findMyComments(req);
        long total = boardCommentMapper.countComments(req);

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .totalPages((int) Math.ceil((double) total / size))
                .totalItems(total)
                .build();

        return ApiResponse.success(comments, pagination);
    }

    /** 단건 조회 */
    public ApiResponse<BoardCommentDetailResponse> findCommentById(Long id) {
        BoardCommentDetailResponse comment = boardCommentMapper.findCommentById(id);
        return ApiResponse.success(comment);
    }
}
