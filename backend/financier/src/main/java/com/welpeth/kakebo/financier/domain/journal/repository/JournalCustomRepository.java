package com.welpeth.kakebo.financier.domain.journal.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.journal.dto.UpdateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import jakarta.transaction.Transactional;
import java.util.UUID;

public interface JournalCustomRepository extends BaseCustomRepository<Journal> {

  @Transactional
  void update(UpdateJournalRequest request);

  @Transactional
  void recalculateTotal(UUID journalId);
}
