package com.welpeth.kakebo.financier.domain.subscription.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.subscription.dto.UpdateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import com.welpeth.kakebo.financier.domain.subscription.entity.QSubscription;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
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
}
