package com.adhd.ad_hell.advertise.command.application.dto.request;

import com.adhd.ad_hell.advertise.command.domain.aggregate.FileType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdCreateRequest {
    private final Long adId;
    private final Long boardId;
    private final Long rewardId;
    private final String fileTitle;
    private final FileType fileType;
    private final String filePath;
}
