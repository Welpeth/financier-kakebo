package com.welpeth.kakebo.financier.domain.installmentPurchase.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.UpdateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.QInstallmentPurchase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public class InstallmentPurchaseCustomRepositoryImpl extends
    BaseCustomRepositoryImpl<InstallmentPurchase>
    implements InstallmentPurchaseCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @Override
  public void update(UpdateInstallmentPurchaseRequest request) {
    QInstallmentPurchase entity = QInstallmentPurchase.installmentPurchase;

    long rows = new JPAUpdateClause(entityManager, entity)
        .set(entity.totalAmount, request.totalAmount())
        .set(entity.interestRate, request.interestRate())
        .set(entity.updatedAt, LocalDateTime.now())
        .where(entity.id.eq(request.id()))
        .execute();

    if (rows < 1) {
      throw new NoResultException("InstallmentPurchase não encontrada");
    }
    entityManager.clear();
  }
}
