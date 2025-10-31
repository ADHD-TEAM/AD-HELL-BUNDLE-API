package com.adhd.ad_hell.domain.reward.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RewardDto {
  private Long id;
  private String name;
  private String description;
  private Integer pointCost;
  private Integer stock;
  private String status;

  private Long categoryId;
  private String categoryName;
  private Long parentCategoryId;
  private String parentCategoryName;
}
