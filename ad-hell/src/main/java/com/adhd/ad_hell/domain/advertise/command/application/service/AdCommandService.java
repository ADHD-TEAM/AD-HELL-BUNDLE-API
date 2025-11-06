package com.adhd.ad_hell.domain.advertise.command.application.service;

import com.adhd.ad_hell.common.storage.FileStorage;
import com.adhd.ad_hell.common.storage.FileStorageResult;
import com.adhd.ad_hell.common.util.SecurityUtil;
import com.adhd.ad_hell.domain.advertise.command.application.dto.request.AdCreateRequest;
import com.adhd.ad_hell.domain.advertise.command.application.dto.request.AdUpdateRequest;
import com.adhd.ad_hell.domain.advertise.command.domain.aggregate.Ad;
import com.adhd.ad_hell.domain.advertise.command.domain.aggregate.AdFile;
import com.adhd.ad_hell.domain.advertise.command.domain.aggregate.AdStatus;
import com.adhd.ad_hell.domain.advertise.command.domain.repository.AdRepository;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdCommandService {

    private final AdRepository adRepository;
    private final FileStorage fileStorage;
    private final SecurityUtil securityUtil;

    @Transactional
    public Long createAd(AdCreateRequest req, List<MultipartFile> videoFiles) {
        Long userId = securityUtil.getLoginUserInfo().getUserId();
        Ad ad = Ad.fromCreateDto(req, userId);
        adRepository.save(ad);

        List<String> storedNames = new ArrayList<>();

        if (videoFiles != null && !videoFiles.isEmpty()) {
            for (MultipartFile file : videoFiles) {
                if (file == null || file.isEmpty()) continue;

                FileStorageResult result = fileStorage.store(file);

                storedNames.add(result.getStoredName());
                AdFile adFile = AdFile.of(result.getStoredName(), file.getOriginalFilename(), result.getUrl());
                ad.addFile(adFile);
            }
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_COMMITTED) {
                    storedNames.forEach(fileStorage::deleteQuietly);
                }
            }
        });

        if (true) throw new RuntimeException("🔥 강제 롤백 테스트 중입니다.");

        return ad.getAdId();
    }

//    @Transactional
//    public void deleteAd(AdCreateRequest req) {
//        adRepository.deleteById(req.getAdId());
//    }

    @Transactional
    public void updateAd(AdUpdateRequest req) {
        Ad ad = adRepository.findById(req.getAdId())
                .orElseThrow(() -> new BusinessException(ErrorCode.AD_NOT_FOUND));

        ad.updateAd(
                req.getTitle(),
                req.getLike_count(),
                req.getBookmark_count(),
                req.getComment_count(),
                req.getView_count()
        );
    }
}
