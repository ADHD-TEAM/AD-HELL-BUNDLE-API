package com.adhd.ad_hell.advertise.command.domain.repository;

import com.adhd.ad_hell.advertise.command.domain.aggregate.AdFile;

import java.util.List;
import java.util.Optional;

public interface AdFileRepository {
    AdFile save(AdFile ad);
    List<AdFile> findByAdFile_AdId(Long adId);
    Optional<AdFile> findById(Long fileId);
    void deleteById(Long fileId);
}