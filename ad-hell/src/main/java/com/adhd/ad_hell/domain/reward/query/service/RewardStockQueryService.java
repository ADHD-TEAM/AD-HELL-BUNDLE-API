package com.adhd.ad_hell.domain.reward.query.service;

import com.adhd.ad_hell.domain.reward.query.dto.response.RewardStockResponse;
import com.adhd.ad_hell.domain.reward.query.mapper.RewardMapper;
import com.adhd.ad_hell.domain.reward.query.mapper.RewardStockMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardStockQueryService {

  private final RewardStockMapper rewardStockMapper;

  public List<RewardStockResponse> getRewardStockList(Long rewardId) {
    return rewardStockMapper.findRewardStocks(rewardId);
  }
}
