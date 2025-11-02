package com.adhd.ad_hell.category.command.infrastructure.repository;

import com.adhd.ad_hell.category.command.domain.aggregate.Category;
import com.adhd.ad_hell.category.command.domain.repository.CategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends CategoryRepository, JpaRepository<Category, Long> {

}
