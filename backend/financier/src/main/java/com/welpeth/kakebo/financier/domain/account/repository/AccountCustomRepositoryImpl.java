package com.welpeth.kakebo.financier.domain.account.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.account.dto.UpdateAccountRequest;
import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.account.entity.QAccount;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

public class AccountCustomRepositoryImpl extends BaseCustomRepositoryImpl<Account>
    implements AccountCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @Override
  public void update(UpdateAccountRequest request) {
    QAccount account = QAccount.account;

    long rowsAffected = new JPAUpdateClause(entityManager, account)
        .set(account.name, request.name())
        .set(account.balance, request.balance())
        .set(account.isActive, request.isActive())
        .set(account.type, request.type())
        .set(account.updatedAt, LocalDateTime.now())
        .where(account.id.eq(request.id()))
        .execute();

    if(rowsAffected < 1) {
      throw new NoResultException("Dashboard nao encontrada");
    }

    entityManager.clear();
  }
}
