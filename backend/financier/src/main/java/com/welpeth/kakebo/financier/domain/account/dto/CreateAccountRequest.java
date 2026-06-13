package com.welpeth.kakebo.financier.domain.account.dto;

import com.welpeth.kakebo.financier.domain.account.type.AccountType;
import java.math.BigDecimal;

public record CreateAccountRequest(
    String name,
    Boolean isActive,
    BigDecimal balance,
    AccountType type
    ) {
}
