package com.welpeth.kakebo.financier.domain.installmentPurchase.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateInstallmentPurchaseRequest(
    UUID id,
    BigDecimal totalAmount,
    Integer installmentCount,
    BigDecimal interestRate
) {
}
