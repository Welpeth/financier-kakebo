package com.welpeth.kakebo.financier.domain.subscription.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.subscription.dto.UpdateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import com.welpeth.kakebo.financier.domain.subscription.entity.QSubscription;
import com.welpeth.kakebo.financier.domain.transaction.entity.QTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionCustomRepositoryImpl extends BaseCustomRepositoryImpl<Subscription>
    implements SubscriptionCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @Override
  public void update(UpdateSubscriptionRequest request) {
    QSubscription entity = QSubscription.subscription;

    long rows = new JPAUpdateClause(entityManager, entity)
        .set(entity.frequency, request.frequency())
        .set(entity.nextChargeDate, request.nextChargeDate())
        .set(entity.active, request.active())
        .set(entity.updatedAt, LocalDateTime.now())
        .where(entity.id.eq(request.id()))
        .execute();

    if (rows < 1) {
      throw new NoResultException("Subscription não encontrada");
    }
    entityManager.clear();
  }

  @Override
  public BigDecimal sumActiveAmountByCardId(UUID cardId, LocalDate endOfMonth) {
    QSubscription subscription = QSubscription.subscription;
    QTransaction transaction = QTransaction.transaction;

    BigDecimal total = getQueryFactory()
        .select(transaction.amount.sum())
        .from(subscription)
        .join(subscription.transaction, transaction)
        .where(transaction.accountCard.id.eq(cardId)
            .and(subscription.active.isTrue())
            .and(subscription.nextChargeDate.loe(endOfMonth)))
        .fetchOne();

    return total != null ? total : BigDecimal.ZERO;
  }

  @Override
  public List<Subscription> findUpcomingActive(LocalDate limit) {
    QSubscription subscription = QSubscription.subscription;
    QTransaction transaction = QTransaction.transaction;

    return getQueryFactory()
        .selectFrom(subscription)
        .join(subscription.transaction, transaction).fetchJoin()
        .where(subscription.active.isTrue()
            .and(subscription.nextChargeDate.loe(limit)))
        .orderBy(subscription.nextChargeDate.asc())
        .fetch();
  }
}
