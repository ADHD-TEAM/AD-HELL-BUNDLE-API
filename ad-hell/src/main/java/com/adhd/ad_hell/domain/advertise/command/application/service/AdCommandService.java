package com.adhd.ad_hell.domain.advertise.command.application.service;

import com.adhd.ad_hell.domain.advertise.command.domain.aggregate.AdFile;
import com.adhd.ad_hell.domain.advertise.command.domain.repository.AdFileRepository;
import com.adhd.ad_hell.domain.advertise.command.domain.repository.AdRepository;
import com.adhd.ad_hell.common.storage.FileStorage;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import com.adhd.ad_hell.domain.advertise.command.application.dto.request.AdCreateRequest;
import com.adhd.ad_hell.domain.advertise.command.application.dto.request.AdUpdateRequest;
import com.adhd.ad_hell.domain.advertise.command.domain.aggregate.Ad;
import com.adhd.ad_hell.domain.advertise.command.domain.aggregate.AdStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdCommandService {

    private final AdFileRepository adFileRepository;
    private final AdRepository adRepository;
    private final FileStorage fileStorage;

    @Value("${ad.adfile-url}")
    private String AdFile_URL;

    /* 광고 파일 등록 */
    @Transactional
    public Long createAdFile(AdCreateRequest adCreateRequest, MultipartFile adContent) {

        // 파일을 먼저 저장하고 실패 시 DB 작업을 수행하지 않음
        final String newFileName = fileStorage.store(adContent);

        // 1) 연관 Ad 조회 (요청에서 adId를 받는다고 가정)
        Ad ad = adRepository.findByAdId(adCreateRequest.getAdId())
                .orElseThrow(() -> new BusinessException(ErrorCode.AD_NOT_FOUND));


        // DTO to Entity
        AdFile newAdFile = AdFile.builder()
                .fileTitle(adCreateRequest.getFileTitle())
                .fileType(adCreateRequest.getFileType())
                .filePath(adCreateRequest.getFilePath())
                .build();

        // 저장 된 이미지를 요청할 url 설정
        newAdFile.changeAdUrl(AdFile_URL + newFileName);

        // 3) 연관관계 편의 메서드 사용
        ad.addFile(newAdFile);

        // Entity 저장
        AdFile saved = adFileRepository.save(newAdFile);

        // 로직 롤백 될 경우 새 파일 제거 -> 롤백 보상
        // TransactionSynchronizationManager : 스프링 트랜잭션이 라이프사이클 이벤트에
        // 외부 로직(파일 삭제, 로그 기록) 등을 안전하게 연결할 수 있는 훅을 제공
        TransactionSynchronizationManager.registerSynchronization(
                // registerSynchronization 메소드를 통해 TransactionSynchronization 구현체
                // 를 등록하면 트랜잭션의 커밋 또는 롤백 직후 로직을 정의할 수 있음
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if(status != STATUS_COMMITTED) {
                            fileStorage.deleteQuietly(newFileName);
                        }
                    }
                }
        );

        return saved.getAd().getAdId();
    }

    @Transactional
    public void updateAdFile(Long adId, AdUpdateRequest req, List<MultipartFile> newFiles) {

        // 1) Ad 조회
        Ad ad = adRepository.findByAdId(adId)
                .orElseThrow(() -> new BusinessException(ErrorCode.AD_NOT_FOUND));

        // 2) 기존 파일 엔티티 & 물리 파일명 확보 (커밋 후 삭제 대상)
        List<AdFile> oldEntities = adFileRepository.findByAd_AdId(adId);
        List<String> oldFileNames = oldEntities.stream()
                .map(AdFile::getFileTitle)
                .filter(Objects::nonNull)
                .toList();

        // 3) 새 파일 저장 (롤백 시 제거 대상 기록)
        List<String> savedNewFileNames = new ArrayList<>();
        if (newFiles != null && !newFiles.isEmpty()) {
            // 전체 교체 전략: 기존 연관 제거 → orphanRemoval 로 DELETE
            ad.clearFiles();

            for (MultipartFile part : newFiles) {
                String saved = fileStorage.store(part);     // 물리 저장
                savedNewFileNames.add(saved);

                String newUrl = AdFile_URL + saved;
                ad.addFile(AdFile.of(newUrl, saved));       // 양방향 연결
            }
        }

        // 4) 광고의 나머지 필드 업데이트
        ad.updateAd(
                req.getTitle(),
                req.getLike_count(),
                req.getBookmark_count(),
                req.getComment_count(),
                req.getView_count()
        );

        // 5) 트랜잭션 종료 훅(커밋/롤백)에서 물리 파일 정리
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            final List<String> finalsOld = List.copyOf(oldFileNames);
            final List<String> finalsNew = List.copyOf(savedNewFileNames);

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    if (status == STATUS_COMMITTED) {
                        for (String old : finalsOld) fileStorage.deleteQuietly(old);
                    } else {
                        for (String nv : finalsNew) fileStorage.deleteQuietly(nv);
                    }
                }
            });
        }
    }

    /* 상품 삭제 */
    @Transactional
    public void deleteAd(Long AdId) {
        adRepository.deleteById(AdId);
    }

}