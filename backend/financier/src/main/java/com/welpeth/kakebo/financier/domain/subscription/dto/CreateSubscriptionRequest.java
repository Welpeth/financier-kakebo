package com.welpeth.kakebo.financier.domain.subscription.dto;

import com.welpeth.kakebo.financier.domain.subscription.type.SubscriptionFrequency;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import java.time.LocalDate;

public record CreateSubscriptionRequest(
    Transaction transaction,
    SubscriptionFrequency frequency,
    LocalDate nextChargeDate
) {
}
