package com.welpeth.kakebo.financier.domain.ledgerEntry.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.CreateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.UpdateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.LedgerEntry;
import com.welpeth.kakebo.financier.domain.ledgerEntry.service.LedgerEntryService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.LEDGER_ENTRY)
public class LedgerEntryController {

  private final LedgerEntryService ledgerEntryService;

  @GetMapping("/{id}")
  public ResponseEntity<LedgerEntry> getById(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(ledgerEntryService.get(id));
  }

  @GetMapping("/list")
  public ResponseEntity<List<LedgerEntry>> getList() {
    return ResponseEntity.status(HttpStatus.OK).body(ledgerEntryService.getList());
  }

  @GetMapping("/list/{journalId}")
  public ResponseEntity<List<LedgerEntry>> getListByJournal(@PathVariable UUID journalId) {
    return ResponseEntity.status(HttpStatus.OK).body(ledgerEntryService.getListByJournal(journalId));
  }

  @PostMapping
  public ResponseEntity<LedgerEntry> create(@RequestBody CreateLedgerEntryRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ledgerEntryService.create(request));
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody UpdateLedgerEntryRequest request) {
    ledgerEntryService.update(request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    ledgerEntryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
