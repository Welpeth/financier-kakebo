package com.welpeth.kakebo.financier.domain.journal.dto;

import java.util.UUID;

public record UpdateJournalRequest(
    UUID id,
    String name
) {
}
