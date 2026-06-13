package com.welpeth.kakebo.financier.domain.transaction.dto;

import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import com.welpeth.kakebo.financier.domain.transaction.type.TransactionType;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateTransactionRequest(
    UUID id,
    Account account,
    AccountCard accountCard,
    TransactionType type,
    BigDecimal fee,
    Integer installment,
    String description
) {
}
