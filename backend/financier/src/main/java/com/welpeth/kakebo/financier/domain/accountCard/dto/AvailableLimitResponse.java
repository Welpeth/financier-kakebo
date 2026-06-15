package com.welpeth.kakebo.financier.domain.accountCard.dto;

import java.math.BigDecimal;

public record AvailableLimitResponse(
    BigDecimal creditLimit,
    BigDecimal usedAmount,
    BigDecimal availableLimit
) {}
