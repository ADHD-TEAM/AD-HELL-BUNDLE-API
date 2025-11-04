package com.adhd.ad_hell.domain.auth.command.service;

public interface AuthRedisService {
    void saveValidityCode(String email, String code);
    Boolean getValidateCode(String email);
}
