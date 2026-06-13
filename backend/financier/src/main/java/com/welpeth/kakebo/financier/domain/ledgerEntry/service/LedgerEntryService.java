package com.welpeth.kakebo.financier.domain.ledgerEntry.service;

import com.welpeth.kakebo.financier.domain.journal.service.JournalService;
import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.CreateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.UpdateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.LedgerEntry;
import com.welpeth.kakebo.financier.domain.ledgerEntry.repository.LedgerEntryRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LedgerEntryService {

  private final LedgerEntryRepository repository;
  @Lazy
  private final JournalService journalService;

  public LedgerEntry create(CreateLedgerEntryRequest request) {
    if (request.journal() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Diário obrigatório");
    }
    if (request.transaction() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transação obrigatória");
    }

    LedgerEntry ledgerEntry = new LedgerEntry();
    ledgerEntry.setId(UUID.randomUUID());
    ledgerEntry.setName(request.name());
    ledgerEntry.setFinalDate(request.finalDate());
    ledgerEntry.setJournal(request.journal());
    ledgerEntry.setTransaction(request.transaction());

    repository.save(ledgerEntry);
    journalService.recalculateTotal(request.journal().getId());
    return ledgerEntry;
  }

  public LedgerEntry get(UUID id) {
    return repository.getReferenceById(id);
  }

  public void update(UpdateLedgerEntryRequest request) {
    repository.update(request);
  }

  public void delete(UUID id) {
    LedgerEntry entry = repository.findById(id)
        .orElseThrow(() -> new jakarta.persistence.NoResultException("LedgerEntry não encontrado"));
    UUID journalId = entry.getJournal().getId();
    repository.deleteById(id);
    journalService.recalculateTotal(journalId);
  }

  public List<LedgerEntry> getList() {
    return repository.findAll();
  }

  public List<LedgerEntry> getListByJournal(UUID journalId) {
    return repository.findByJournalId(journalId);
  }
}
