package com.adhd.ad_hell.category.command.domain.repository;

import com.adhd.ad_hell.category.command.domain.aggregate.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository {

  Optional<Category> findById(Long categoryId);

  Category save(Category category);
}
