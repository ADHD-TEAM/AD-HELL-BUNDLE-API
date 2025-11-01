package com.adhd.ad_hell.domain.reward.query.mapper;

import com.adhd.ad_hell.domain.reward.query.dto.response.RewardDetailResponse;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardDto;
import com.adhd.ad_hell.domain.reward.query.dto.response.RewardResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RewardMapper {
  RewardDto findRewardById(@Param("rewardId") Long rewardId);
  List<RewardDto> findRewards(@Param("keyword") String keyword);
}
