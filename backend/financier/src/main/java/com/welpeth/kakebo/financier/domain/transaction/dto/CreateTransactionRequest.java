package com.welpeth.kakebo.financier.domain.transaction.dto;

import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import com.welpeth.kakebo.financier.domain.category.entity.Category;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.subscription.type.SubscriptionFrequency;
import com.welpeth.kakebo.financier.domain.transaction.type.InstallmentType;
import com.welpeth.kakebo.financier.domain.transaction.type.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransactionRequest(
    Account account,
    AccountCard accountCard,
    Category category,
    TransactionType type,
    BigDecimal amount,
    BigDecimal fee,
    Integer installment,
    String description,
    LocalDate dueDate,
    SubscriptionFrequency frequency,
    InstallmentType installmentType,
    Journal journal
) {

}
