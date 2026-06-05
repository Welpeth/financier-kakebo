package com.welpeth.kakebo.financier.domain.journal.repository;

import com.querydsl.jpa.impl.JPAUpdateClause;
import com.welpeth.kakebo.financier.base.BaseCustomRepositoryImpl;
import com.welpeth.kakebo.financier.domain.journal.dto.UpdateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.journal.entity.QJournal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

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
        .set(journal.totalValue, request.totalValue())
        .set(journal.updatedAt, LocalDateTime.now())
        .where(journal.id.eq(request.id()))
        .execute();

    if(rowsAffected < 1) {
      throw new NoResultException("Dashboard nao encontrada");
    }

    entityManager.clear();
  }
}
