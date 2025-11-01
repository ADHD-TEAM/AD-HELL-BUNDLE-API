package com.adhd.ad_hell.domain.reward.query.service;

import com.adhd.ad_hell.domain.reward.query.dto.response.RewardDetailResponse;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardDto;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardResponse;
import com.adhd.ad_hell.domain.reward.query.mapper.RewardMapper;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RewardQueryService {

  private final RewardMapper rewardMapper;

  @Transactional(readOnly = true)
  public RewardDetailResponse getRewardDetail(Long rewardId) {
    RewardDto dto = rewardMapper.findRewardById(rewardId);
    if (dto == null) {
      throw new BusinessException(ErrorCode.REWARD_NOT_FOUND);
    }
    return RewardDetailResponse.from(dto);
  }

  @Transactional(readOnly = true)
  public List<RewardResponse> getRewardList(String keyword) {
    List<RewardDto> rewardDtos = rewardMapper.findRewards(keyword);
    List<RewardResponse> responses = new ArrayList<>();
    for (RewardDto dto : rewardDtos) {
      RewardResponse response = RewardResponse.from(dto);
      responses.add(response);
    }

    return responses;
//    return rewardMapper.findRewards(keyword).stream()
//                       .map(RewardResponse::from)
//                       .toList();
  }
}
