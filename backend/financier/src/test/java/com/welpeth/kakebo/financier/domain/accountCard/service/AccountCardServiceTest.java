package com.welpeth.kakebo.financier.domain.accountCard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.accountCard.dto.AvailableLimitResponse;
import com.welpeth.kakebo.financier.domain.accountCard.dto.CreateAccountCardRequest;
import com.welpeth.kakebo.financier.domain.accountCard.dto.UpdateAccountCardRequest;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import com.welpeth.kakebo.financier.domain.accountCard.repository.AccountCardRepository;
import com.welpeth.kakebo.financier.domain.accountCard.type.CardType;
import com.welpeth.kakebo.financier.domain.installment.repository.InstallmentRepository;
import com.welpeth.kakebo.financier.domain.subscription.repository.SubscriptionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountCardServiceTest {

  @Mock
  private AccountCardRepository repository;
  @Mock
  private InstallmentRepository installmentRepository;
  @Mock
  private SubscriptionRepository subscriptionRepository;

  @InjectMocks
  private AccountCardService service;

  @Test
  @DisplayName("create mapeia os campos do request e persiste o cartão")
  void createMapsFieldsAndSaves() {
    // Arrange
    Account account = new Account();
    CreateAccountCardRequest request = new CreateAccountCardRequest(
        "Cartão Nubank", true, CardType.CREDIT, new BigDecimal("1000"), 12, 2030, account);

    // Act
    AccountCard created = service.create(request);

    // Assert
    assertThat(created.getId()).isNotNull();
    assertThat(created.getName()).isEqualTo("Cartão Nubank");
    assertThat(created.getIsActive()).isTrue();
    assertThat(created.getType()).isEqualTo(CardType.CREDIT);
    assertThat(created.getCreditLimit()).isEqualByComparingTo("1000");
    assertThat(created.getExpirationMonth()).isEqualTo(12);
    assertThat(created.getExpirationYear()).isEqualTo(2030);
    assertThat(created.getAccount()).isSameAs(account);
    verify(repository).save(created);
  }

  @Test
  @DisplayName("getAvailableLimit subtrai parcelas e assinaturas do limite do cartão")
  void getAvailableLimitSubtractsUsedAmounts() {
    // Arrange
    UUID cardId = UUID.randomUUID();
    AccountCard card = new AccountCard();
    card.setCreditLimit(new BigDecimal("1000"));
    when(repository.getReferenceById(cardId)).thenReturn(card);
    when(installmentRepository.sumUnpaidAmountByCardId(cardId)).thenReturn(new BigDecimal("300"));
    when(subscriptionRepository.sumActiveAmountByCardId(eq(cardId), any(LocalDate.class)))
        .thenReturn(new BigDecimal("200"));

    // Act
    AvailableLimitResponse response = service.getAvailableLimit(cardId);

    // Assert
    assertThat(response.creditLimit()).isEqualByComparingTo("1000");
    assertThat(response.usedAmount()).isEqualByComparingTo("500");
    assertThat(response.availableLimit()).isEqualByComparingTo("500");
  }

  @Test
  @DisplayName("getAvailableLimit trata limite nulo como zero, resultando em limite disponível negativo")
  void getAvailableLimitTreatsNullLimitAsZero() {
    // Arrange
    UUID cardId = UUID.randomUUID();
    AccountCard card = new AccountCard();
    card.setCreditLimit(null);
    when(repository.getReferenceById(cardId)).thenReturn(card);
    when(installmentRepository.sumUnpaidAmountByCardId(cardId)).thenReturn(new BigDecimal("100"));
    when(subscriptionRepository.sumActiveAmountByCardId(eq(cardId), any(LocalDate.class)))
        .thenReturn(BigDecimal.ZERO);

    // Act
    AvailableLimitResponse response = service.getAvailableLimit(cardId);

    // Assert
    assertThat(response.creditLimit()).isEqualByComparingTo(BigDecimal.ZERO);
    assertThat(response.usedAmount()).isEqualByComparingTo("100");
    assertThat(response.availableLimit()).isEqualByComparingTo("-100");
  }

  @Test
  @DisplayName("get retorna o cartão referenciado pelo id")
  void getReturnsReferencedCard() {
    // Arrange
    UUID id = UUID.randomUUID();
    AccountCard card = new AccountCard();
    when(repository.getReferenceById(id)).thenReturn(card);

    // Act
    AccountCard result = service.get(id);

    // Assert
    assertThat(result).isSameAs(card);
  }

  @Test
  @DisplayName("update delega ao repositório")
  void updateDelegatesToRepository() {
    // Arrange
    UpdateAccountCardRequest request = new UpdateAccountCardRequest(
        UUID.randomUUID(), "Cartão", false, CardType.DEBIT, new BigDecimal("500"), 1, 2031);

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

  @Test
  @DisplayName("getList retorna todos os cartões")
  void getListReturnsAll() {
    // Arrange
    List<AccountCard> cards = List.of(new AccountCard(), new AccountCard());
    when(repository.findAll()).thenReturn(cards);

    // Act
    List<AccountCard> result = service.getList();

    // Assert
    assertThat(result).isSameAs(cards);
  }

  @Test
  @DisplayName("getListByAccount delega ao repositório com o id da conta")
  void getListByAccountDelegates() {
    // Arrange
    UUID accountId = UUID.randomUUID();
    List<AccountCard> cards = List.of(new AccountCard());
    when(repository.findByAccountId(accountId)).thenReturn(cards);

    // Act
    List<AccountCard> result = service.getListByAccount(accountId);

    // Assert
    assertThat(result).isSameAs(cards);
  }
}
