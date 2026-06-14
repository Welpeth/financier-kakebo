package com.welpeth.kakebo.financier.domain.journal.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.journal.dto.UpdateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.journal.entity.QJournal;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.QLedgerEntry;
import com.welpeth.kakebo.financier.domain.transaction.entity.QTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class JournalCustomRepositoryImpl extends BaseCustomRepositoryImpl<Journal> implements
    JournalCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @Override
  public void update(UpdateJournalRequest request) {
    QJournal journal = QJournal.journal;

    long rowsAffected = new JPAUpdateClause(entityManager, journal)
        .set(journal.name, request.name())
        .set(journal.updatedAt, LocalDateTime.now())
        .where(journal.id.eq(request.id()))
        .execute();

    if (rowsAffected < 1) {
      throw new NoResultException("Journal não encontrado");
    }

    entityManager.clear();
  }

  @Transactional
  @Override
  public void recalculateTotal(UUID journalId) {
    QLedgerEntry ledgerEntry = QLedgerEntry.ledgerEntry;
    QTransaction transaction = QTransaction.transaction;
    QJournal journal = QJournal.journal;

    BigDecimal total = getQueryFactory()
        .select(transaction.amount.sum())
        .from(ledgerEntry)
        .join(ledgerEntry.transaction, transaction)
        .where(ledgerEntry.journal.id.eq(journalId))
        .fetchOne();

    new JPAUpdateClause(entityManager, journal)
        .set(journal.totalValue, total != null ? total : BigDecimal.ZERO)
        .set(journal.updatedAt, LocalDateTime.now())
        .where(journal.id.eq(journalId))
        .execute();

    entityManager.clear();
  }
}
