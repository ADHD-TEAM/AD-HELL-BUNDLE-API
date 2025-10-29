package com.adhd.ad_hell.category.command.application.service;

import com.adhd.ad_hell.category.command.application.dto.request.CreateCategoryRequest;
import com.adhd.ad_hell.category.command.application.dto.request.UpdateCategoryRequest;
import com.adhd.ad_hell.category.command.domain.aggregate.Category;
import com.adhd.ad_hell.category.command.domain.aggregate.CategoryStatus;
import com.adhd.ad_hell.category.command.domain.repository.CategoryRepository;
import com.adhd.ad_hell.exception.BusinessException;
import com.adhd.ad_hell.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryCommandService {

  private final CategoryRepository categoryRepository;

  @Transactional
  public void createCategory(CreateCategoryRequest req) {

    Category parentCategory = null;
    if (req.getParentId() != null) {
      parentCategory = categoryRepository.findById(req.getParentId())
                                         .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    Category newCategory = Category.builder()
        .name(req.getName())
        .description(req.getDescription())
        .parent(parentCategory)
        .status(CategoryStatus.ACTIVATE)
        .build();

    categoryRepository.save(newCategory);
  }

  public void updateCategory(Long categoryId, UpdateCategoryRequest req) {
  }

  public void deleteCategory(Long categoryId) {
  }
}
