package com.adhd.ad_hell.domain.reward.query.dto.response;

import com.adhd.ad_hell.common.dto.CategoryInfoResponse;
import com.adhd.ad_hell.domain.reward.command.domain.aggregate.RewardStatus;
import com.adhd.ad_hell.domain.reward.command.domain.aggregate.RewardStock;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RewardResponse {
  private final Long id;
  private final String name;
  private final Integer pointCost;
  private final Integer stock;
  private final RewardStatus status;
  private CategoryInfoResponse category;

  public static RewardResponse from(RewardDto dto) {
    return RewardResponse.builder()
                         .id(dto.getId())
                         .name(dto.getName())
                         .pointCost(dto.getPointCost())
                         .stock(dto.getStock())
                         .status(dto.getStatus())
                         .category(CategoryInfoResponse.builder()
                                                       .categoryId(dto.getCategoryId())
                                                       .categoryName(dto.getCategoryName())
                                                       .parentId(dto.getParentCategoryId())
                                                       .parentName(dto.getParentCategoryName())
                                                       .build())
                         .build();
  }
}
