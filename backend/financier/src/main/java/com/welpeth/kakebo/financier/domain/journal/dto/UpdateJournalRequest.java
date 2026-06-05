package com.welpeth.kakebo.financier.domain.journal.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateJournalRequest(
    UUID id,
    String name,
    BigDecimal totalValue
) {
}
