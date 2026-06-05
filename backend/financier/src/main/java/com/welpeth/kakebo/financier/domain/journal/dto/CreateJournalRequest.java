package com.welpeth.kakebo.financier.domain.journal.dto;

import java.math.BigDecimal;

public record CreateJournalRequest(
    String name,
    BigDecimal totalValue
) {
}
