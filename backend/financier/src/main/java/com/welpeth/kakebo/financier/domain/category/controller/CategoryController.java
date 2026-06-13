package com.welpeth.kakebo.financier.domain.category.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.category.dto.CreateCategoryRequest;
import com.welpeth.kakebo.financier.domain.category.dto.UpdateCategoryRequest;
import com.welpeth.kakebo.financier.domain.category.entity.Category;
import com.welpeth.kakebo.financier.domain.category.service.CategoryService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.CATEGORY)
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/{id}")
  public ResponseEntity<Category> getById(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.get(id));
  }

  @GetMapping("/list")
  public ResponseEntity<List<Category>> getList() {
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.getList());
  }

  @PostMapping
  public ResponseEntity<Category> create(@RequestBody CreateCategoryRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody UpdateCategoryRequest request) {
    categoryService.update(request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
