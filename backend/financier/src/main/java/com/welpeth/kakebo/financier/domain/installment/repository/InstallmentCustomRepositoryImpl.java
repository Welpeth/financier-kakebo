package com.welpeth.kakebo.financier.domain.installment.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.installment.dto.UpdateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import com.welpeth.kakebo.financier.domain.installment.entity.QInstallment;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.QInstallmentPurchase;
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

  @Override
  public BigDecimal sumUnpaidAmountByCardId(UUID cardId) {
    QInstallment installment = QInstallment.installment;
    QInstallmentPurchase purchase = QInstallmentPurchase.installmentPurchase;
    QTransaction transaction = QTransaction.transaction;

    BigDecimal total = getQueryFactory()
        .select(installment.amount.sum())
        .from(installment)
        .join(installment.installmentPurchase, purchase)
        .join(purchase.transaction, transaction)
        .where(transaction.accountCard.id.eq(cardId)
            .and(installment.paid.isFalse()))
        .fetchOne();

    return total != null ? total : BigDecimal.ZERO;
  }

  @Override
  public List<Installment> findByCardId(UUID cardId) {
    QInstallment installment = QInstallment.installment;
    QInstallmentPurchase purchase = QInstallmentPurchase.installmentPurchase;
    QTransaction transaction = QTransaction.transaction;

    return getQueryFactory()
        .selectFrom(installment)
        .join(installment.installmentPurchase, purchase).fetchJoin()
        .join(purchase.transaction, transaction).fetchJoin()
        .where(transaction.accountCard.id.eq(cardId))
        .orderBy(installment.dueDate.asc())
        .fetch();
  }

  @Override
  public List<Installment> findUpcomingUnpaid(LocalDate limit) {
    QInstallment installment = QInstallment.installment;
    QInstallmentPurchase purchase = QInstallmentPurchase.installmentPurchase;
    QTransaction transaction = QTransaction.transaction;

    return getQueryFactory()
        .selectFrom(installment)
        .join(installment.installmentPurchase, purchase).fetchJoin()
        .join(purchase.transaction, transaction).fetchJoin()
        .where(installment.paid.isFalse()
            .and(installment.dueDate.loe(limit)))
        .orderBy(installment.dueDate.asc())
        .fetch();
  }
}
