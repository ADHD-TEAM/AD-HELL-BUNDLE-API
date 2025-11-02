package com.adhd.ad_hell.advertise.command.domain.repository;

import com.adhd.ad_hell.advertise.command.domain.aggregate.Ad;
import java.util.Optional;

public interface AdRepository {
    Ad save(Ad ad);
    // AdFile.ad.adId 경로에 맞게 메서드명 수정
    List<AdFile> findByAd_AdId(Long adId);
    Optional<Ad> findById(Long adId);
    void deleteById(Long adId);
}