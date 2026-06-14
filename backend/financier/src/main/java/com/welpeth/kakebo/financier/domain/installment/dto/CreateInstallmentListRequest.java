package com.welpeth.kakebo.financier.domain.installment.dto;

import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateInstallmentListRequest(
    InstallmentPurchase installmentPurchase,
    int installmentNumber,
    BigDecimal amount,
    BigDecimal fee,
    LocalDate dueDate
) {

}
