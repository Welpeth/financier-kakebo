package com.welpeth.kakebo.financier.domain.category.service;

import com.welpeth.kakebo.financier.domain.category.dto.CreateCategoryRequest;
import com.welpeth.kakebo.financier.domain.category.dto.UpdateCategoryRequest;
import com.welpeth.kakebo.financier.domain.category.entity.Category;
import com.welpeth.kakebo.financier.domain.category.repository.CategoryRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository repository;

  public Category create(CreateCategoryRequest request) {
    Category category = new Category();
    category.setId(UUID.randomUUID());
    category.setName(request.name());
    category.setJournal(request.journal());

    repository.save(category);
    return category;
  }

  public Category get(UUID id) {
    return repository.getReferenceById(id);
  }

  public void update(UpdateCategoryRequest request) {
    repository.update(request);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }

  public List<Category> getList() {
    return repository.findAll();
  }
}
