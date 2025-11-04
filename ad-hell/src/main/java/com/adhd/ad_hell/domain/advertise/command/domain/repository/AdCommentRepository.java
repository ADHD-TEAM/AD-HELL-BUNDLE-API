package com.adhd.ad_hell.domain.advertise.command.domain.repository;

import com.adhd.ad_hell.domain.advertise.command.domain.aggregate.AdComment;

import java.util.Optional;

public interface AdCommentRepository {
    AdComment save(AdComment adComment);
    Optional<AdComment> findById(Long adComment);
    void deleteById(Long adComment);
}
