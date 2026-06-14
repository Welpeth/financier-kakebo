package com.welpeth.kakebo.financier.domain.installment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateInstallmentRequest(
    UUID id,
    BigDecimal amount,
    LocalDate dueDate,
    Boolean paid,
    LocalDate paidAt
) {
}
