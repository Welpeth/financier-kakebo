package com.welpeth.kakebo.financier.domain.accountCard.dto;

import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.accountCard.type.CardType;
import java.math.BigDecimal;

public record CreateAccountCardRequest(
    String name,
    Boolean isActive,
    CardType type,
    BigDecimal creditLimit,
    Integer expirationMonth,
    Integer expirationYear,
    Account account
) {
}
