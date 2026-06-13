package com.welpeth.kakebo.financier.domain.ledgerEntry.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.UpdateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.LedgerEntry;
import jakarta.transaction.Transactional;

public interface LedgerEntryCustomRepository extends BaseCustomRepository<LedgerEntry> {

  @Transactional
  void update(UpdateLedgerEntryRequest request);
}
