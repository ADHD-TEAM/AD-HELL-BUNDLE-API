package com.adhd.ad_hell.domain.board.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class BoardCommandResponse {

    private Long id;
    private String title;
    private String writerId;
    private String content;
    private String categoryId;
    private String status;
    private Long viewCount;
}
