package com.adhd.ad_hell.domain.board_comment.query.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardCommentSearchRequest {

    private Integer page;     // 현재 페이지 (기본값 1)
    private Integer size;     // 페이지당 게시물 수 (기본값 20)
    private Long boardId;     // 게시판 ID
    private Long writerId;    // 작성자 ID (내 댓글 조회용)
    private String keyword;   // 내용 검색용
}
