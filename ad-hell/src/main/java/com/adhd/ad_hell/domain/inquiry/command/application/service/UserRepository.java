package com.adhd.ad_hell.domain.inquiry.command.application.service;

import com.adhd.ad_hell.domain.user.command.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
