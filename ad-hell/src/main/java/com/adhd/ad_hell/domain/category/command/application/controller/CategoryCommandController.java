package com.adhd.ad_hell.domain.category.command.application.controller;

import com.adhd.ad_hell.domain.category.command.application.dto.request.CreateCategoryRequest;
import com.adhd.ad_hell.domain.category.command.application.dto.request.UpdateCategoryRequest;
import com.adhd.ad_hell.domain.category.command.application.service.CategoryCommandService;
import com.adhd.ad_hell.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryCommandController {

  private final CategoryCommandService categoryCommandService;

  /* 기본 반환 전부 void 처리, 추후 변경 필요 */
  @PostMapping
  public ResponseEntity<ApiResponse<Void>> createCategory(@RequestBody CreateCategoryRequest req) {
    categoryCommandService.createCategory(req);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(ApiResponse.success(null));
  }

  @PutMapping("/{categoryId}")
  public ResponseEntity<ApiResponse<Void>> updateCategory(@PathVariable Long categoryId, @RequestBody UpdateCategoryRequest req) {
    categoryCommandService.updateCategory(categoryId, req);
    return ResponseEntity.ok(ApiResponse.success(null));
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long categoryId) {
    categoryCommandService.deleteCategory(categoryId);
    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
