package com.welpeth.kakebo.financier.domain.transaction.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.transaction.dto.UpdateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.entity.QTransaction;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

public class TransactionCustomRepositoryImpl extends BaseCustomRepositoryImpl<Transaction> implements
    TransactionCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @Override
  public void update(UpdateTransactionRequest request) {
    QTransaction transaction = QTransaction.transaction;

    long rowsAffected = new JPAUpdateClause(entityManager, transaction)
        .set(transaction.type, request.type())
        .set(transaction.amount, request.amount())
        .set(transaction.fee, request.fee() != null ? request.fee() : java.math.BigDecimal.ZERO)
        .set(transaction.installment, request.installment())
        .set(transaction.account, request.account())
        .set(transaction.accountCard, request.accountCard())
        .set(transaction.category, request.category())
        .set(transaction.updatedAt, LocalDateTime.now())
        .where(transaction.id.eq(request.id()))
        .execute();

    if(rowsAffected < 1) {
      throw new NoResultException("Dashboard nao encontrada");
    }

    entityManager.clear();
  }
}
