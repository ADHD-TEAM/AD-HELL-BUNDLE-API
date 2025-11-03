package com.adhd.ad_hell.domain.user.command.service;

import com.adhd.ad_hell.domain.user.command.dto.request.UserPointRequest;
import com.adhd.ad_hell.domain.user.command.dto.response.UserPointResponse;
import com.adhd.ad_hell.domain.user.command.entity.User;
import com.adhd.ad_hell.domain.user.command.repository.UserCommandRepository;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPointCommandServiceImpl implements UserPointCommandService {

  private final UserCommandRepository userCommandRepository;

  @Transactional
  @Override
  public UserPointResponse earnPoints(UserPointRequest userPointRequest) {

    User findUser = userCommandRepository.findByLoginId(userPointRequest.getLoginId())
                                         .orElseThrow(() ->new BusinessException(ErrorCode.USER_NOT_FOUND));

    findUser.earnPoint(userPointRequest.getPoint());
    return UserPointResponse.builder()
        .amount(findUser.getAmount())
                            .build();
  }
}
