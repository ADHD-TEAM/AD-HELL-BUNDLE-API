package com.adhd.ad_hell.domain.advertise.command.application.controller;

import com.adhd.ad_hell.domain.advertise.command.application.dto.request.AdCreateRequest;
import com.adhd.ad_hell.domain.advertise.command.application.dto.request.AdUpdateRequest;
import com.adhd.ad_hell.domain.advertise.command.application.service.AdCommandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

// ... existing code ...

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ads")
public class AdCommandController {

    private final AdCommandService adCommandService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createAd(
        @RequestPart("adInfo") AdCreateRequest req,               // JSON 데이터
        @RequestPart(value = "videoFiles", required = false) List<MultipartFile> videoFiles // 여러 파일
    ) {
        Long adId = adCommandService.createAd(req, videoFiles);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(Map.of("adId", adId));
    }

    @PostMapping(value = "/delete", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> deleteAd(@RequestBody AdCreateRequest req) {
//        adCommandService.deleteAd(req);
        return ResponseEntity.noContent().build();
    }


    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<Map<String, Object>> updateAd(@RequestBody AdUpdateRequest req) {
        adCommandService.updateAd(req);
        return ResponseEntity.noContent().build();
    }
}
