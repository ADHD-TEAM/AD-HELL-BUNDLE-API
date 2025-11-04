package com.adhd.ad_hell.domain.auth.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthRedisServiceImpl implements AuthRedisService {

    private final long VALIDITY_SECONDS = 5; // 인증번호 기본 시간

    private final RedisTemplate<String, Object> redisTemplate;

    // 인증번호 저장
    @Override
    public void saveValidityCode(String email, String code) {
        String key = "email:verity:" + email;
        log.info("[AuthRedisServiceImpl/saveValidityCode] 인증번호 저장| email={} code={} key={}", email, code, key);
        log.info("[AuthRedisServiceImpl/saveValidityCode] 인증번호 저장| code,VALIDITY_SECONDS = {} TimeUnit.MINUTES={}", code,VALIDITY_SECONDS, TimeUnit.MINUTES);

        redisTemplate.opsForValue()
                .set(key, code,VALIDITY_SECONDS, TimeUnit.MINUTES);

        log.info("[AuthRedisServiceImpl/saveValidityCode] 인증번호 저장 성공 | ");
    }

    // 인증번호 확인
    @Override
    public Boolean getValidateCode(String email) {
        log.info("[AuthRedisServiceImpl/getValidateCode]  email={} ", email);
        String key = "email:verity:" + email;
        Boolean exist = redisTemplate.hasKey(key);
        log.info("[AuthRedisServiceImpl/getValidateCode]  exist={} ", exist);
        return Boolean.TRUE.equals(exist);
    }
}
