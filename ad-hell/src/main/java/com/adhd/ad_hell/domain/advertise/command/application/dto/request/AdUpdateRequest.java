package com.adhd.ad_hell.domain.advertise.command.application.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdUpdateRequest {
    String title;
    int like_count;
    int bookmark_count;
    int comment_count;
    int view_count;
}