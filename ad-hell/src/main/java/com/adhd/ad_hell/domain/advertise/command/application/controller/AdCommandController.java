package com.adhd.ad_hell.domain.advertise.command.application.controller;

import com.adhd.ad_hell.common.dto.ApiResponse;
import com.adhd.ad_hell.domain.advertise.command.application.dto.request.AdCreateRequest;
import com.adhd.ad_hell.domain.advertise.command.application.dto.request.AdUpdateRequest;
import com.adhd.ad_hell.domain.advertise.command.application.dto.response.AdCommandResponse;
import com.adhd.ad_hell.domain.advertise.command.application.service.AdCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adFiles")
public class AdCommandController {

    private final AdCommandService adCommandService;

    // 광고 생성: JSON + 파일(multipart/form-data)
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<AdCommandResponse>> createAd(
            @RequestPart AdCreateRequest adCreateRequest,
            @RequestPart MultipartFile adContent
    ) {
        Long adId = adCommandService.createAd(adCreateRequest, adContent);
        AdCommandResponse response = AdCommandResponse.builder()
                .adId(adId)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    // 광고 수정: 파일 전체 교체 전략(JSON + 다중 파일)
    @PutMapping(value = "/{adId}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<Void>> updateAd(
            @PathVariable Long adId,
            @RequestPart AdUpdateRequest adUpdateRequest,
            @RequestPart(required = false) List<MultipartFile> newFiles
    ) {
        adCommandService.updateAdWithFiles(adId, adUpdateRequest, newFiles);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 광고 삭제(soft delete 또는 설정에 따름)
    @DeleteMapping("/{adId}")
    public ResponseEntity<ApiResponse<Void>> deleteAd(
            @PathVariable Long adId
    ) {
        adCommandService.deleteAd(adId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
