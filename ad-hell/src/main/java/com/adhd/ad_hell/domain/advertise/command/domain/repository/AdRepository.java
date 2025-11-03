package com.adhd.ad_hell.domain.advertise.command.domain.repository;

import com.adhd.ad_hell.domain.advertise.command.domain.aggregate.Ad;

import java.util.List;
import java.util.Optional;

public interface AdRepository {
    Ad save(Ad ad);
    // AdFile.ad.adId 경로에 맞게 메서드명 수정
    List<Ad> findByAd_AdId(Long adId);
    Optional<Ad> findById(Long adId);
    void deleteById(Long adId);
}