package com.adhd.ad_hell.domain.auth.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
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
    public Boolean existVerificationCode(String email, String code) {
        String key = "email:verity:" + email;
        String originCode = redisTemplate.opsForValue().get(key).toString();

        log.info("[AuthRedisServiceImpl/getValidateCode]  email={} ", email);
        log.info("[AuthRedisServiceImpl/getValidateCode]  originCode={} ", originCode);

        // 레디스에 있는 인증코드와 받은 코드가 같은지 확인
        return Objects.equals(originCode, code);
    }
}
