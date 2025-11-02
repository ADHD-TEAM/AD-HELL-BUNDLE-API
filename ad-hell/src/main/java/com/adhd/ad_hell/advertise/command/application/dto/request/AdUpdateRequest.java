package com.adhd.ad_hell.advertise.command.application.dto.request;

import com.adhd.ad_hell.advertise.command.domain.aggregate.FileType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdUpdateRequest {
    private final String title;
    private final int like_count;
    private final int bookmark_count;
    private final int comment_count;
    private final int view_count;
}