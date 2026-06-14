package com.welpeth.kakebo.financier.domain.subscription.scheduler;

import com.welpeth.kakebo.financier.domain.subscription.dto.UpdateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import com.welpeth.kakebo.financier.domain.subscription.repository.SubscriptionRepository;
import com.welpeth.kakebo.financier.domain.subscription.type.SubscriptionFrequency;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

  private final SubscriptionRepository subscriptionRepository;

  @Scheduled(cron = "0 0 0 * * *")
  public void processSubscriptions() {
    LocalDate today = LocalDate.now();
    List<Subscription> due = subscriptionRepository
        .findAllByActiveTrueAndNextChargeDateLessThanEqual(today);

    for (Subscription sub : due) {
      LocalDate next = sub.getNextChargeDate();
      while (!next.isAfter(today)) {
        next = advanceDate(next, sub.getFrequency());
      }
      subscriptionRepository.update(new UpdateSubscriptionRequest(
          sub.getId(), sub.getFrequency(), next, sub.getActive()
      ));
    }
  }

  private LocalDate advanceDate(LocalDate from, SubscriptionFrequency frequency) {
    return switch (frequency) {
      case DAILY -> from.plusDays(1);
      case WEEKLY -> from.plusWeeks(1);
      case MONTHLY -> from.plusMonths(1);
      case QUARTERLY -> from.plusMonths(3);
      case YEARLY -> from.plusYears(1);
    };
  }
}
