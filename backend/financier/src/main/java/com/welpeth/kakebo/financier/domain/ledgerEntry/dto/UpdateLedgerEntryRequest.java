package com.welpeth.kakebo.financier.domain.ledgerEntry.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateLedgerEntryRequest(
    UUID id,
    String name,
    LocalDateTime finalDate
) {
}
