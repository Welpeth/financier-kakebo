package com.welpeth.kakebo.financier.domain.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.subscription.dto.CreateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.dto.UpdateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import com.welpeth.kakebo.financier.domain.subscription.repository.SubscriptionRepository;
import com.welpeth.kakebo.financier.domain.subscription.type.SubscriptionFrequency;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

  @Mock
  private SubscriptionRepository repository;

  @InjectMocks
  private SubscriptionService service;

  @Test
  @DisplayName("create gera id, mapeia os campos e marca como ativa")
  void createMapsFieldsAndFlagsActive() {
    // Arrange
    Transaction transaction = new Transaction();
    LocalDate nextCharge = LocalDate.of(2026, Month.JANUARY, 10);
    CreateSubscriptionRequest request =
        new CreateSubscriptionRequest(transaction, SubscriptionFrequency.MONTHLY, nextCharge);
    when(repository.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    Subscription created = service.create(request);

    // Assert
    assertThat(created.getId()).isNotNull();
    assertThat(created.getTransaction()).isSameAs(transaction);
    assertThat(created.getFrequency()).isEqualTo(SubscriptionFrequency.MONTHLY);
    assertThat(created.getNextChargeDate()).isEqualTo(nextCharge);
    assertThat(created.getActive()).isTrue();
  }

  @Test
  @DisplayName("get lança 404 quando a assinatura não existe")
  void getThrowsWhenMissing() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.empty());

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.get(id));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(ex.getReason()).isEqualTo("Subscription não encontrada");
  }

  @Test
  @DisplayName("payCurrentPeriod avança um dia para frequência diária")
  void payCurrentPeriodAdvancesDaily() {
    // Arrange / Act / Assert
    assertNextChargeAfterPayment(
        SubscriptionFrequency.DAILY,
        LocalDate.of(2026, Month.JANUARY, 10),
        LocalDate.of(2026, Month.JANUARY, 11));
  }

  @Test
  @DisplayName("payCurrentPeriod avança uma semana para frequência semanal")
  void payCurrentPeriodAdvancesWeekly() {
    assertNextChargeAfterPayment(
        SubscriptionFrequency.WEEKLY,
        LocalDate.of(2026, Month.JANUARY, 10),
        LocalDate.of(2026, Month.JANUARY, 17));
  }

  @Test
  @DisplayName("payCurrentPeriod avança um mês para frequência mensal")
  void payCurrentPeriodAdvancesMonthly() {
    assertNextChargeAfterPayment(
        SubscriptionFrequency.MONTHLY,
        LocalDate.of(2026, Month.JANUARY, 10),
        LocalDate.of(2026, Month.FEBRUARY, 10));
  }

  @Test
  @DisplayName("payCurrentPeriod avança três meses para frequência trimestral")
  void payCurrentPeriodAdvancesQuarterly() {
    assertNextChargeAfterPayment(
        SubscriptionFrequency.QUARTERLY,
        LocalDate.of(2026, Month.JANUARY, 10),
        LocalDate.of(2026, Month.APRIL, 10));
  }

  @Test
  @DisplayName("payCurrentPeriod avança um ano para frequência anual")
  void payCurrentPeriodAdvancesYearly() {
    assertNextChargeAfterPayment(
        SubscriptionFrequency.YEARLY,
        LocalDate.of(2026, Month.JANUARY, 10),
        LocalDate.of(2027, Month.JANUARY, 10));
  }

  @Test
  @DisplayName("getByTransaction delega ao repositório")
  void getByTransactionDelegates() {
    // Arrange
    UUID transactionId = UUID.randomUUID();
    List<Subscription> subscriptions = List.of(new Subscription());
    when(repository.findByTransactionId(transactionId)).thenReturn(subscriptions);

    // Act
    List<Subscription> result = service.getByTransaction(transactionId);

    // Assert
    assertThat(result).isSameAs(subscriptions);
  }

  @Test
  @DisplayName("getList retorna todas as assinaturas")
  void getListReturnsAll() {
    // Arrange
    List<Subscription> subscriptions = List.of(new Subscription(), new Subscription());
    when(repository.findAll()).thenReturn(subscriptions);

    // Act
    List<Subscription> result = service.getList();

    // Assert
    assertThat(result).isSameAs(subscriptions);
  }

  @Test
  @DisplayName("update delega ao repositório")
  void updateDelegatesToRepository() {
    // Arrange
    UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(
        UUID.randomUUID(), SubscriptionFrequency.MONTHLY, LocalDate.now(), true);

    // Act
    service.update(request);

    // Assert
    verify(repository).update(request);
  }

  @Test
  @DisplayName("delete remove pelo id")
  void deleteRemovesById() {
    // Arrange
    UUID id = UUID.randomUUID();

    // Act
    service.delete(id);

    // Assert
    verify(repository).deleteById(id);
  }

  private void assertNextChargeAfterPayment(
      SubscriptionFrequency frequency, LocalDate current, LocalDate expected) {
    // Arrange
    UUID id = UUID.randomUUID();
    Subscription subscription = new Subscription();
    subscription.setId(id);
    subscription.setFrequency(frequency);
    subscription.setNextChargeDate(current);
    when(repository.findById(id)).thenReturn(Optional.of(subscription));
    when(repository.save(any(Subscription.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    Subscription result = service.payCurrentPeriod(id);

    // Assert
    assertThat(result.getNextChargeDate()).isEqualTo(expected);
  }
}
