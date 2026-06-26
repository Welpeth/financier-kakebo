package com.welpeth.kakebo.financier.domain.subscription.scheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.subscription.dto.UpdateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import com.welpeth.kakebo.financier.domain.subscription.repository.SubscriptionRepository;
import com.welpeth.kakebo.financier.domain.subscription.type.SubscriptionFrequency;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubscriptionSchedulerTest {

  @Mock
  private SubscriptionRepository subscriptionRepository;

  @InjectMocks
  private SubscriptionScheduler scheduler;

  @Test
  @DisplayName("processSubscriptions avança a próxima cobrança para depois de hoje recuperando períodos perdidos")
  void processAdvancesPastDueSubscriptionBeyondToday() {
    // Arrange
    LocalDate today = LocalDate.now();
    Subscription subscription = new Subscription();
    subscription.setId(UUID.randomUUID());
    subscription.setFrequency(SubscriptionFrequency.MONTHLY);
    subscription.setNextChargeDate(today.minusMonths(5));
    subscription.setActive(true);
    when(subscriptionRepository.findAllByActiveTrueAndNextChargeDateLessThanEqual(any(LocalDate.class)))
        .thenReturn(List.of(subscription));

    // Act
    scheduler.processSubscriptions();

    // Assert
    ArgumentCaptor<UpdateSubscriptionRequest> captor =
        ArgumentCaptor.forClass(UpdateSubscriptionRequest.class);
    verify(subscriptionRepository).update(captor.capture());
    UpdateSubscriptionRequest updated = captor.getValue();
    assertThat(updated.id()).isEqualTo(subscription.getId());
    assertThat(updated.nextChargeDate()).isAfter(today);
    assertThat(updated.frequency()).isEqualTo(SubscriptionFrequency.MONTHLY);
    assertThat(updated.active()).isTrue();
  }

  @Test
  @DisplayName("processSubscriptions avança exatamente um período quando a cobrança vence hoje")
  void processAdvancesOnePeriodWhenDueToday() {
    // Arrange
    LocalDate today = LocalDate.now();
    Subscription subscription = new Subscription();
    subscription.setId(UUID.randomUUID());
    subscription.setFrequency(SubscriptionFrequency.WEEKLY);
    subscription.setNextChargeDate(today);
    subscription.setActive(true);
    when(subscriptionRepository.findAllByActiveTrueAndNextChargeDateLessThanEqual(any(LocalDate.class)))
        .thenReturn(List.of(subscription));

    // Act
    scheduler.processSubscriptions();

    // Assert
    ArgumentCaptor<UpdateSubscriptionRequest> captor =
        ArgumentCaptor.forClass(UpdateSubscriptionRequest.class);
    verify(subscriptionRepository).update(captor.capture());
    assertThat(captor.getValue().nextChargeDate()).isEqualTo(today.plusWeeks(1));
  }

  @Test
  @DisplayName("processSubscriptions não atualiza nada quando não há assinaturas vencidas")
  void processDoesNothingWhenNoneDue() {
    // Arrange
    when(subscriptionRepository.findAllByActiveTrueAndNextChargeDateLessThanEqual(any(LocalDate.class)))
        .thenReturn(List.of());

    // Act
    scheduler.processSubscriptions();

    // Assert
    verify(subscriptionRepository, never()).update(any());
  }
}
