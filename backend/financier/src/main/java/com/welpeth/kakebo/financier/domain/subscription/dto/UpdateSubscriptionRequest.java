package com.welpeth.kakebo.financier.domain.subscription.dto;

import com.welpeth.kakebo.financier.domain.subscription.type.SubscriptionFrequency;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateSubscriptionRequest(
    UUID id,
    SubscriptionFrequency frequency,
    LocalDate nextChargeDate,
    Boolean active
) {
}
