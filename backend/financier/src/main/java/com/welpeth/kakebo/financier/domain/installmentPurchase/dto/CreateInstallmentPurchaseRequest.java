package com.welpeth.kakebo.financier.domain.installmentPurchase.dto;

import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import java.math.BigDecimal;

public record CreateInstallmentPurchaseRequest(
    Transaction transaction,
    BigDecimal totalAmount,
    Integer installmentCount,
    BigDecimal interestRate
) {
}
