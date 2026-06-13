package com.welpeth.kakebo.financier.domain.ledgerEntry.service;

import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.CreateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.UpdateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.LedgerEntry;
import com.welpeth.kakebo.financier.domain.ledgerEntry.repository.LedgerEntryRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LedgerEntryService {

  private final LedgerEntryRepository repository;

  public LedgerEntry create(CreateLedgerEntryRequest request) {
    LedgerEntry ledgerEntry = new LedgerEntry();
    ledgerEntry.setId(UUID.randomUUID());
    ledgerEntry.setName(request.name());
    ledgerEntry.setFinalDate(request.finalDate());
    ledgerEntry.setJournal(request.journal());
    ledgerEntry.setTransaction(request.transaction());

    repository.save(ledgerEntry);
    return ledgerEntry;
  }

  public LedgerEntry get(UUID id) {
    return repository.getReferenceById(id);
  }

  public void update(UpdateLedgerEntryRequest request) {
    repository.update(request);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }

  public List<LedgerEntry> getList() {
    return repository.findAll();
  }
}
