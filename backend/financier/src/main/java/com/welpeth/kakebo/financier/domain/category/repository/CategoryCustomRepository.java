package com.welpeth.kakebo.financier.domain.category.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.category.dto.UpdateCategoryRequest;
import com.welpeth.kakebo.financier.domain.category.entity.Category;
import jakarta.transaction.Transactional;

public interface CategoryCustomRepository extends BaseCustomRepository<Category> {

  @Transactional
  void update(UpdateCategoryRequest request);
}
