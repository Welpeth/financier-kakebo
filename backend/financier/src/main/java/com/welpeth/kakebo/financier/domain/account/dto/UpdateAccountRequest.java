package com.welpeth.kakebo.financier.domain.account.dto;

import com.welpeth.kakebo.financier.domain.account.type.AccountType;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateAccountRequest(
    UUID id,
    String name,
    Boolean isActive,
    BigDecimal balance,
    AccountType type
) {

}
