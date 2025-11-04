package com.adhd.ad_hell.domain.user.command.service;

import com.adhd.ad_hell.common.util.SecurityUtil;
import com.adhd.ad_hell.domain.user.command.dto.request.UserPointRequest;
import com.adhd.ad_hell.domain.user.command.dto.response.UserPointResponse;
import com.adhd.ad_hell.domain.user.command.entity.PointHistory;
import com.adhd.ad_hell.domain.user.command.entity.PointType;
import com.adhd.ad_hell.domain.user.command.entity.User;
import com.adhd.ad_hell.domain.user.command.repository.PointCommandRepository;
import com.adhd.ad_hell.domain.user.command.repository.UserCommandRepository;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointCommandServiceImpl implements PointCommandService {

  private final UserCommandRepository userCommandRepository;
  private final PointCommandRepository pointCommandRepository;
  private final SecurityUtil securityUtil;

  @Transactional
  @Override
  public UserPointResponse earnPoints(UserPointRequest userPointRequest) {
    User findUser = userCommandRepository.findByIdForUpdate(
        securityUtil.getLoginUserInfo().getUserId()
    ).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

    findUser.earnPoint(userPointRequest.getPoint());

    PointHistory pointHistory = PointHistory.builder()
        .user(findUser)
        .changeAmount(userPointRequest.getPoint().intValue())
        .pointType(PointType.VIEW)
        .build();

    pointCommandRepository.save(pointHistory);

    return UserPointResponse.builder()
        .amount(findUser.getAmount())
                            .build();
  }
}
