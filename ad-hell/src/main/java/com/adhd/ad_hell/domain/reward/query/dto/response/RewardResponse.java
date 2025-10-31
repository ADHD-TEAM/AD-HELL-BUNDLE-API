package com.adhd.ad_hell.domain.reward.query.dto.response;

import com.adhd.ad_hell.common.dto.CategoryInfoResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RewardResponse {
  private final Long id;
  private final String name;
  private final Integer pointCost;
  private final Integer stock;
  private CategoryInfoResponse category;

  public static RewardResponse from(RewardDto dto) {
    return RewardResponse.builder()
                         .id(dto.getId())
                         .name(dto.getName())
                         .pointCost(dto.getPointCost())
                         .stock(dto.getStock())
                         .category(CategoryInfoResponse.builder()
                                                       .categoryId(dto.getCategoryId())
                                                       .categoryName(dto.getCategoryName())
                                                       .parentId(dto.getParentCategoryId())
                                                       .parentName(dto.getParentCategoryName())
                                                       .build())
                         .build();
  }
}
