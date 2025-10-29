package com.adhd.ad_hell.category.command.application.dto.request;

import com.adhd.ad_hell.category.command.domain.aggregate.CategoryStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateCategoryRequest {
  private final Long parentId;
  private final String name;
  private final String description;
  private final CategoryStatus status;
}
