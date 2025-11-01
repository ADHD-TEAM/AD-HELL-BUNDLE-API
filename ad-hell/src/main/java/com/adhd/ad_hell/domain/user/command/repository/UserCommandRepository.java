package com.adhd.ad_hell.domain.user.command.repository;

import com.adhd.ad_hell.domain.user.command.entity.User;
import com.adhd.ad_hell.domain.user.query.dto.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCommandRepository extends JpaRepository<User, Long> {
    // 사용가능한 닉네임인지 확인
    Boolean existsByloginId(String loginId);
    Boolean existsByNickname(String nickname);

    Optional<User> findByLoginId(String loginId);
    Optional<User> findByUserId(Long userId);
}
