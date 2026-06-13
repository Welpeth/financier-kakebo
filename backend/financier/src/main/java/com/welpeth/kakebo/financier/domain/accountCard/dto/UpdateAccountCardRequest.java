package com.welpeth.kakebo.financier.domain.accountCard.dto;

import com.welpeth.kakebo.financier.domain.accountCard.type.CardType;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateAccountCardRequest(
    UUID id,
    String name,
    Boolean isActive,
    CardType type,
    BigDecimal creditLimit,
    Integer expirationMonth,
    Integer expirationYear
) {
}
