package com.welpeth.kakebo.financier.base;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import com.welpeth.kakebo.financier.util.Paged;
import java.util.List;

public interface BaseCustomRepository<T> {

  JPAQueryFactory getQueryFactory();

  Paged<T> findAll(EntityPath<T> tableReference, Predicate filters, List<OrderSpecifier<?>> orderList, int size,
      int pageIndex, List<EntityPath<?>> leftJoins);

}