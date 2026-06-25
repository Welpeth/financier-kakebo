package com.welpeth.kakebo.financier.domain.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.category.dto.CreateCategoryRequest;
import com.welpeth.kakebo.financier.domain.category.dto.UpdateCategoryRequest;
import com.welpeth.kakebo.financier.domain.category.entity.Category;
import com.welpeth.kakebo.financier.domain.category.repository.CategoryRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Mock
  private CategoryRepository repository;

  @InjectMocks
  private CategoryService service;

  @Test
  @DisplayName("create gera id, mapeia o nome e persiste a categoria")
  void createMapsFieldsAndSaves() {
    // Arrange
    CreateCategoryRequest request = new CreateCategoryRequest("Alimentação");

    // Act
    Category created = service.create(request);

    // Assert
    assertThat(created.getId()).isNotNull();
    assertThat(created.getName()).isEqualTo("Alimentação");
    verify(repository).save(created);
  }

  @Test
  @DisplayName("get retorna a categoria referenciada pelo id")
  void getReturnsReferencedCategory() {
    // Arrange
    UUID id = UUID.randomUUID();
    Category category = new Category();
    when(repository.getReferenceById(id)).thenReturn(category);

    // Act
    Category result = service.get(id);

    // Assert
    assertThat(result).isSameAs(category);
  }

  @Test
  @DisplayName("update delega ao repositório")
  void updateDelegatesToRepository() {
    // Arrange
    UpdateCategoryRequest request = new UpdateCategoryRequest(UUID.randomUUID(), "Lazer");

    // Act
    service.update(request);

    // Assert
    verify(repository).update(request);
  }

  @Test
  @DisplayName("delete remove pelo id")
  void deleteRemovesById() {
    // Arrange
    UUID id = UUID.randomUUID();

    // Act
    service.delete(id);

    // Assert
    verify(repository).deleteById(id);
  }

  @Test
  @DisplayName("getList retorna todas as categorias")
  void getListReturnsAll() {
    // Arrange
    List<Category> categories = List.of(new Category(), new Category());
    when(repository.findAll()).thenReturn(categories);

    // Act
    List<Category> result = service.getList();

    // Assert
    assertThat(result).isSameAs(categories);
  }

  @Test
  @DisplayName("create não persiste nada além da própria categoria criada")
  void createSavesExactlyTheCreatedEntity() {
    // Arrange
    CreateCategoryRequest request = new CreateCategoryRequest("Transporte");

    // Act
    Category created = service.create(request);

    // Assert
    verify(repository).save(any(Category.class));
    assertThat(created.getName()).isEqualTo("Transporte");
  }
}
