package com.adhd.ad_hell.domain.category.query.controller;

import com.adhd.ad_hell.domain.category.query.dto.response.CategoryDetailResponse;
import com.adhd.ad_hell.domain.category.query.dto.response.CategoryTreeResponse;
import com.adhd.ad_hell.domain.category.query.service.CategoryQueryService;
import com.adhd.ad_hell.common.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryQueryController {

  private final CategoryQueryService categoryQueryService;

  @GetMapping("/{categoryId}")
  public ResponseEntity<ApiResponse<CategoryDetailResponse>> getCategoryDetails(
      @PathVariable Long categoryId
  ) {
    CategoryDetailResponse response = categoryQueryService.getCategoryDetail(categoryId);
    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<CategoryTreeResponse>>> getCategories(
      @RequestParam(required = false) String keyword
  ) {
    List<CategoryTreeResponse> tree = categoryQueryService.getCategoryTree(keyword);
    return ResponseEntity.ok(ApiResponse.success(tree));
  }
}
