package com.welpeth.kakebo.financier.domain.journal.service;

import com.welpeth.kakebo.financier.domain.journal.dto.CreateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.dto.UpdateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.journal.repository.JournalRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JournalService {

  private final JournalRepository repository;

  public Journal create(CreateJournalRequest request) {
    Journal journal = new Journal();
    journal.setId(UUID.randomUUID());
    journal.setName(request.name());
    journal.setTotalValue(BigDecimal.ZERO);

    repository.save(journal);
    return journal;
  }

  public Journal get(UUID id) {
    return repository.getReferenceById(id);
  }

  public void update(UpdateJournalRequest request) {
    repository.update(request);
  }

  public void recalculateTotal(UUID journalId) {
    repository.recalculateTotal(journalId);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }

  public List<Journal> getList() {
    return repository.findAll();
  }
}
