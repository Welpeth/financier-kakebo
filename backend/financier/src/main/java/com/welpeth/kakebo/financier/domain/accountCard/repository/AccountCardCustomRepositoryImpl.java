package com.welpeth.kakebo.financier.domain.accountCard.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.accountCard.dto.UpdateAccountCardRequest;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import com.welpeth.kakebo.financier.domain.accountCard.entity.QAccountCard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

public class AccountCardCustomRepositoryImpl extends BaseCustomRepositoryImpl<AccountCard>
    implements AccountCardCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @Override
  public void update(UpdateAccountCardRequest request) {
    QAccountCard accountCard = QAccountCard.accountCard;

    long rowsAffected = new JPAUpdateClause(entityManager, accountCard)
        .set(accountCard.name, request.name())
        .set(accountCard.isActive, request.isActive())
        .set(accountCard.type, request.type())
        .set(accountCard.creditLimit, request.creditLimit())
        .set(accountCard.expirationMonth, request.expirationMonth())
        .set(accountCard.expirationYear, request.expirationYear())
        .set(accountCard.updatedAt, LocalDateTime.now())
        .where(accountCard.id.eq(request.id()))
        .execute();

    if (rowsAffected < 1) {
      throw new NoResultException("AccountCard nao encontrado");
    }

    entityManager.clear();
  }
}
