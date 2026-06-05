package com.welpeth.kakebo.financier.domain.journal.service;

import com.welpeth.kakebo.financier.domain.journal.dto.CreateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.dto.GetJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.dto.UpdateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.journal.repository.JournalRepository;
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
    journal.setTotalValue(request.totalValue());

    repository.save(journal);
    return journal;
  }

  public Journal get(UUID id) {
    return repository.findById(id).orElse(null);
  }

  public void update(UpdateJournalRequest request) {
    repository.update(request);
  }
}
