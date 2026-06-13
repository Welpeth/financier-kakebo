package com.welpeth.kakebo.financier.domain.ledgerEntry.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.UpdateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.LedgerEntry;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.QLedgerEntry;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

public class LedgerEntryCustomRepositoryImpl extends BaseCustomRepositoryImpl<LedgerEntry>
    implements LedgerEntryCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  @Override
  public void update(UpdateLedgerEntryRequest request) {
    QLedgerEntry ledgerEntry = QLedgerEntry.ledgerEntry;

    long rowsAffected = new JPAUpdateClause(entityManager, ledgerEntry)
        .set(ledgerEntry.name, request.name())
        .set(ledgerEntry.finalDate, request.finalDate())
        .set(ledgerEntry.updatedAt, LocalDateTime.now())
        .where(ledgerEntry.id.eq(request.id()))
        .execute();

    if (rowsAffected < 1) {
      throw new NoResultException("LedgerEntry nao encontrada");
    }

    entityManager.clear();
  }
}