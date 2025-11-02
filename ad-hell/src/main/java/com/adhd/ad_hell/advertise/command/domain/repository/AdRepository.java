package com.adhd.ad_hell.advertise.command.domain.repository;

import com.adhd.ad_hell.advertise.command.domain.aggregate.Ad;
import java.util.Optional;

public interface AdRepository {
    Ad save(Ad ad);
    Optional<Ad> findById(Long adId);
    void deleteById(Long adId);
}