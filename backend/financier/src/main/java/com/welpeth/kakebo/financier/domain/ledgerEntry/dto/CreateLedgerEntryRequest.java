package com.welpeth.kakebo.financier.domain.ledgerEntry.dto;

import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import java.time.LocalDateTime;

public record CreateLedgerEntryRequest(
    String name,
    LocalDateTime finalDate,
    Journal journal,
    Transaction transaction
) {
}
