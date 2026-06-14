package com.welpeth.kakebo.financier.domain.installment.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.installment.dto.UpdateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import com.welpeth.kakebo.financier.domain.installment.entity.QInstallment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public class InstallmentCustomRepositoryImpl extends BaseCustomRepositoryImpl<Installment>
    implements InstallmentCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @Override
  public void update(UpdateInstallmentRequest request) {
    QInstallment entity = QInstallment.installment;

    long rows = new JPAUpdateClause(entityManager, entity)
        .set(entity.amount, request.amount())
        .set(entity.dueDate, request.dueDate())
        .set(entity.paid, request.paid())
        .set(entity.paidAt, request.paidAt())
        .set(entity.updatedAt, LocalDateTime.now())
        .where(entity.id.eq(request.id()))
        .execute();

    if (rows < 1) {
      throw new NoResultException("Installment não encontrada");
    }
    entityManager.clear();
  }
}
