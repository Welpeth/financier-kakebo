package com.welpeth.kakebo.financier.domain.category.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.category.dto.UpdateCategoryRequest;
import com.welpeth.kakebo.financier.domain.category.entity.Category;
import com.welpeth.kakebo.financier.domain.category.entity.QCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

public class CategoryCustomRepositoryImpl extends BaseCustomRepositoryImpl<Category>
    implements CategoryCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @Override
  public void update(UpdateCategoryRequest request) {
    QCategory category = QCategory.category;

    long rowsAffected = new JPAUpdateClause(entityManager, category)
        .set(category.name, request.name())
        .set(category.updatedAt, LocalDateTime.now())
        .where(category.id.eq(request.id()))
        .execute();

    if (rowsAffected < 1) {
      throw new NoResultException("Category nao encontrada");
    }

    entityManager.clear();
  }
}
