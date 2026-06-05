package com.welpeth.kakebo.financier.domain.journal.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.journal.dto.CreateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.dto.GetJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.dto.UpdateJournalRequest;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.journal.service.JournalService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.JOURNAL)
public class JournalController {

  private final JournalService journalService;

  @GetMapping("/{id}")
  public ResponseEntity<Journal> getById(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(journalService.get(id));
  }

  @PostMapping
  public ResponseEntity<Journal> create(@RequestBody CreateJournalRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(journalService.create(request));
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody UpdateJournalRequest request) {
    journalService.update(request);
    return ResponseEntity.noContent().build();
  }

}
