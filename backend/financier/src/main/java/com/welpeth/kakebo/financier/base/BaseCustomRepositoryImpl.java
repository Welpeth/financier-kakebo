package com.welpeth.kakebo.financier.base;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.welpeth.kakebo.financier.util.Paged;
import org.springframework.data.repository.NoRepositoryBean;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@NoRepositoryBean
public class BaseCustomRepositoryImpl<T extends BaseEntity> implements BaseCustomRepository<T> {

  @PersistenceContext
  private EntityManager em;

  public void setEntityManager(EntityManager em) {
    this.em = em;
  }

  public JPAQueryFactory getQueryFactory() {
    return new JPAQueryFactory(em);
  }


  public Paged<T> findAll(EntityPath<T> tableReference, Predicate filters, List<OrderSpecifier<?>> orderList,
      int size, int pageIndex, List<EntityPath<?>> leftJoins) {

    JPAQuery<T> query = getQueryFactory().select(tableReference).from(tableReference);

    if (!leftJoins.isEmpty()) {
      leftJoins.forEach(query::leftJoin);
    }

    if (filters != null) {
      query.where(filters);
    }

    long total = query.clone()
        .select(Expressions.stringPath(tableReference, "id")
        .count())
        .fetchFirst();

    if (size == 0) {
      size = 100;
    }

    query.offset((long) pageIndex * size).limit(size);

    query.orderBy(orderList.toArray(OrderSpecifier[]::new));

    return new Paged<>(query.fetch(), total);
  }

}
