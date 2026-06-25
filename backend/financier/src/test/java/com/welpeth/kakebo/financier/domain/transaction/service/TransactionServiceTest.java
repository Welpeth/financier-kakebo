package com.welpeth.kakebo.financier.domain.transaction.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.accountCard.dto.AvailableLimitResponse;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import com.welpeth.kakebo.financier.domain.accountCard.service.AccountCardService;
import com.welpeth.kakebo.financier.domain.category.entity.Category;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import com.welpeth.kakebo.financier.domain.installment.service.InstallmentService;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import com.welpeth.kakebo.financier.domain.installmentPurchase.service.InstallmentPurchaseService;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.ledgerEntry.service.LedgerEntryService;
import com.welpeth.kakebo.financier.domain.subscription.service.SubscriptionService;
import com.welpeth.kakebo.financier.domain.subscription.type.SubscriptionFrequency;
import com.welpeth.kakebo.financier.domain.transaction.dto.CreateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.dto.UpdateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import com.welpeth.kakebo.financier.domain.transaction.repository.TransactionRepository;
import com.welpeth.kakebo.financier.domain.transaction.type.InstallmentType;
import com.welpeth.kakebo.financier.domain.transaction.type.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
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
class TransactionServiceTest {

  @Mock
  private TransactionRepository repository;
  @Mock
  private InstallmentService installmentService;
  @Mock
  private InstallmentPurchaseService installmentPurchaseService;
  @Mock
  private SubscriptionService subscriptionService;
  @Mock
  private AccountCardService accountCardService;
  @Mock
  private LedgerEntryService ledgerEntryService;

  @InjectMocks
  private TransactionService service;

  // ----- validateCreateRequest -----

  @Test
  @DisplayName("validateCreateRequest rejeita request nulo com 400 e mensagem de dados obrigatórios")
  void validateCreateRequestRejectsNull() {
    // Arrange / Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.validateCreateRequest(null));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(ex.getReason()).isEqualTo("Dados obrigatórios");
  }

  @Test
  @DisplayName("validateCreateRequest rejeita request sem categoria com 400")
  void validateCreateRequestRejectsMissingCategory() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder().category(null).build();

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.validateCreateRequest(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(ex.getReason()).isEqualTo("Categoria obrigatória");
  }

  @Test
  @DisplayName("validateCreateRequest rejeita valor abaixo de R$ 1,00 com 400")
  void validateCreateRequestRejectsAmountBelowMinimum() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder().amount(new BigDecimal("0.99")).build();

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.validateCreateRequest(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(ex.getReason()).isEqualTo("Valor mínimo da transação é R$ 1,00");
  }

  @Test
  @DisplayName("validateCreateRequest aceita request válido")
  void validateCreateRequestAcceptsValid() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder().build();

    // Act / Assert
    assertDoesNotThrow(() -> service.validateCreateRequest(request));
  }

  // ----- validateCreditTransaction -----

  @Test
  @DisplayName("validateCreditTransaction não valida limite para transações que não são de crédito")
  void validateCreditSkipsNonCredit() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder().type(TransactionType.CASH).build();

    // Act / Assert
    assertDoesNotThrow(() -> service.validateCreditTransaction(request));
    verify(accountCardService, never()).getAvailableLimit(any());
  }

  @Test
  @DisplayName("validateCreditTransaction rejeita crédito sem cartão informado com 400")
  void validateCreditRejectsMissingCard() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder()
        .type(TransactionType.CREDIT)
        .accountCard(null)
        .build();

    // Act
    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> service.validateCreditTransaction(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(ex.getReason()).isEqualTo("Cartão de crédito obrigatório para transações de crédito");
  }

  @Test
  @DisplayName("validateCreditTransaction rejeita quando o total a comprometer excede o limite disponível")
  void validateCreditRejectsOverLimit() {
    // Arrange
    AccountCard card = accountCard();
    CreateTransactionRequest request = createRequestBuilder()
        .type(TransactionType.CREDIT)
        .accountCard(card)
        .amount(new BigDecimal("100"))
        .build();
    when(accountCardService.getAvailableLimit(card.getId()))
        .thenReturn(new AvailableLimitResponse(
            new BigDecimal("100"), new BigDecimal("50"), new BigDecimal("50")));

    // Act
    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> service.validateCreditTransaction(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(ex.getReason()).startsWith("Limite insuficiente");
  }

  @Test
  @DisplayName("validateCreditTransaction aceita crédito quando há limite suficiente")
  void validateCreditAcceptsWithinLimit() {
    // Arrange
    AccountCard card = accountCard();
    CreateTransactionRequest request = createRequestBuilder()
        .type(TransactionType.CREDIT)
        .accountCard(card)
        .amount(new BigDecimal("100"))
        .build();
    when(accountCardService.getAvailableLimit(card.getId()))
        .thenReturn(new AvailableLimitResponse(
            new BigDecimal("500"), new BigDecimal("0"), new BigDecimal("500")));

    // Act / Assert
    assertDoesNotThrow(() -> service.validateCreditTransaction(request));
  }

  // ----- calculateTotalToCommit -----

  @Test
  @DisplayName("calculateTotalToCommit para assinatura retorna o próprio valor da transação")
  void calculateTotalReturnsAmountForSubscription() {
    // Arrange
    BigDecimal amount = new BigDecimal("250");
    CreateTransactionRequest request = createRequestBuilder()
        .amount(amount)
        .frequency(SubscriptionFrequency.MONTHLY)
        .build();

    // Act
    BigDecimal total = service.calculateTotalToCommit(request);

    // Assert
    assertThat(total).isEqualByComparingTo(amount);
  }

  @Test
  @DisplayName("calculateTotalToCommit para parcelamento com juros retorna valor acima do principal")
  void calculateTotalAddsInterestForInstallments() {
    // Arrange
    BigDecimal amount = new BigDecimal("100");
    CreateTransactionRequest request = createRequestBuilder()
        .amount(amount)
        .frequency(null)
        .installment(3)
        .fee(new BigDecimal("10"))
        .installmentType(InstallmentType.PRICE)
        .build();

    // Act
    BigDecimal total = service.calculateTotalToCommit(request);

    // Assert
    assertThat(total).isGreaterThan(amount);
  }

  @Test
  @DisplayName("calculateTotalToCommit usa 1 parcela e taxa zero quando installment e fee são nulos")
  void calculateTotalUsesDefaultsWhenInstallmentAndFeeNull() {
    // Arrange
    BigDecimal amount = new BigDecimal("100");
    CreateTransactionRequest request = createRequestBuilder()
        .amount(amount)
        .frequency(null)
        .installment(null)
        .fee(null)
        .installmentType(InstallmentType.SAC)
        .build();

    // Act
    BigDecimal total = service.calculateTotalToCommit(request);

    // Assert
    assertThat(total).isEqualByComparingTo(amount);
  }

  // ----- setAndSaveTransaction -----

  @Test
  @DisplayName("setAndSaveTransaction mapeia os campos do request para a entidade salva")
  void setAndSaveMapsFields() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder()
        .amount(new BigDecimal("150"))
        .description("Mercado")
        .type(TransactionType.DEBIT)
        .build();
    when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    Transaction saved = service.setAndSaveTransaction(request);

    // Assert
    assertThat(saved.getAmount()).isEqualByComparingTo("150");
    assertThat(saved.getDescription()).isEqualTo("Mercado");
    assertThat(saved.getType()).isEqualTo(TransactionType.DEBIT);
  }

  @Test
  @DisplayName("setAndSaveTransaction usa fee zero quando a taxa não é informada")
  void setAndSaveDefaultsFeeToZero() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder().fee(null).build();
    when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    Transaction saved = service.setAndSaveTransaction(request);

    // Assert
    assertThat(saved.getFee()).isEqualByComparingTo(BigDecimal.ZERO);
  }

  @Test
  @DisplayName("setAndSaveTransaction marca como assinatura quando há frequência")
  void setAndSaveFlagsSubscription() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder()
        .frequency(SubscriptionFrequency.MONTHLY)
        .build();
    when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    Transaction saved = service.setAndSaveTransaction(request);

    // Assert
    assertThat(saved.isSubscription()).isTrue();
  }

  @Test
  @DisplayName("setAndSaveTransaction não marca como assinatura quando não há frequência")
  void setAndSaveDoesNotFlagSubscription() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder().frequency(null).build();
    when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    Transaction saved = service.setAndSaveTransaction(request);

    // Assert
    assertThat(saved.isSubscription()).isFalse();
  }

  // ----- create -----

  @Test
  @DisplayName("create cria parcelas para transação não recorrente")
  void createCreatesInstallments() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder()
        .type(TransactionType.CASH)
        .frequency(null)
        .journal(null)
        .build();
    when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    service.create(request);

    // Assert
    verify(installmentPurchaseService).create(any());
    verify(installmentService).createList(any());
    verify(subscriptionService, never()).create(any());
  }

  @Test
  @DisplayName("create agenda assinatura para crédito recorrente e não cria parcelas")
  void createSchedulesSubscription() {
    // Arrange
    AccountCard card = accountCard();
    CreateTransactionRequest request = createRequestBuilder()
        .type(TransactionType.CREDIT)
        .accountCard(card)
        .amount(new BigDecimal("100"))
        .frequency(SubscriptionFrequency.MONTHLY)
        .journal(null)
        .build();
    when(accountCardService.getAvailableLimit(card.getId()))
        .thenReturn(new AvailableLimitResponse(
            new BigDecimal("500"), new BigDecimal("0"), new BigDecimal("500")));
    when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    service.create(request);

    // Assert
    verify(subscriptionService).create(any());
    verify(installmentPurchaseService, never()).create(any());
    verify(installmentService, never()).createList(any());
  }

  @Test
  @DisplayName("createTransactionSchedule cai no parcelamento quando é assinatura mas não é crédito")
  void scheduleFallsBackToInstallmentsForNonCreditSubscription() {
    // Arrange
    Transaction saved = new Transaction();
    saved.setId(UUID.randomUUID());
    saved.setSubscription(true);
    saved.setAmount(new BigDecimal("100"));
    saved.setFee(BigDecimal.ZERO);
    saved.setInstallment(1);
    CreateTransactionRequest request = createRequestBuilder()
        .type(TransactionType.CASH)
        .frequency(SubscriptionFrequency.MONTHLY)
        .build();

    // Act
    service.createTransactionSchedule(saved, request);

    // Assert
    verify(installmentPurchaseService).create(any());
    verify(subscriptionService, never()).create(any());
  }

  @Test
  @DisplayName("create cria lançamento contábil quando há journal")
  void createCreatesLedgerEntry() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder()
        .type(TransactionType.CASH)
        .frequency(null)
        .journal(new Journal())
        .build();
    when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    service.create(request);

    // Assert
    verify(ledgerEntryService).create(any());
  }

  @Test
  @DisplayName("create não cria lançamento contábil quando não há journal")
  void createSkipsLedgerEntry() {
    // Arrange
    CreateTransactionRequest request = createRequestBuilder()
        .type(TransactionType.CASH)
        .frequency(null)
        .journal(null)
        .build();
    when(repository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    service.create(request);

    // Assert
    verify(ledgerEntryService, never()).create(any());
  }

  @Test
  @DisplayName("create não salva quando a validação falha")
  void createDoesNotSaveOnValidationFailure() {
    // Arrange / Act
    assertThrows(ResponseStatusException.class, () -> service.create(null));

    // Assert
    verify(repository, never()).save(any());
  }

  // ----- update -----

  @Test
  @DisplayName("update rejeita valor nulo com 400 e não persiste")
  void updateRejectsNullAmount() {
    // Arrange
    UpdateTransactionRequest request = updateRequest(TransactionType.CASH, null);

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.update(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(ex.getReason()).isEqualTo("Valor mínimo da transação é R$ 1,00");
    verify(repository, never()).update(any());
  }

  @Test
  @DisplayName("update rejeita valor abaixo do mínimo com 400")
  void updateRejectsAmountBelowMinimum() {
    // Arrange
    UpdateTransactionRequest request = updateRequest(TransactionType.CASH, new BigDecimal("0.50"));

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.update(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("update atualiza transação não-crédito sem recalcular parcelas")
  void updateNonCreditWithoutRecalculation() {
    // Arrange
    UpdateTransactionRequest request = updateRequest(TransactionType.CASH, new BigDecimal("100"));

    // Act
    service.update(request);

    // Assert
    verify(repository).update(request);
    verify(installmentPurchaseService, never()).getByTransaction(any());
  }

  @Test
  @DisplayName("update de crédito sem compras parceladas não recalcula parcelas")
  void updateCreditWithoutPurchasesDoesNotRecalculate() {
    // Arrange
    UpdateTransactionRequest request = updateRequest(TransactionType.CREDIT, new BigDecimal("100"));
    when(installmentPurchaseService.getByTransaction(request.id())).thenReturn(List.of());

    // Act
    service.update(request);

    // Assert
    verify(installmentPurchaseService, never()).update(any());
    verify(installmentService, never()).deleteByPurchase(any());
    verify(installmentService, never()).createList(any());
  }

  @Test
  @DisplayName("update de crédito com compra parcelada existente recalcula as parcelas")
  void updateCreditWithPurchaseRecalculates() {
    // Arrange
    UpdateTransactionRequest request = updateRequest(TransactionType.CREDIT, new BigDecimal("100"));
    InstallmentPurchase purchase = installmentPurchase();
    when(installmentPurchaseService.getByTransaction(request.id())).thenReturn(List.of(purchase));
    when(installmentService.getByPurchase(purchase.getId()))
        .thenReturn(List.of(installment(1, LocalDate.of(2026, Month.JANUARY, 10))));

    // Act
    service.update(request);

    // Assert
    verify(installmentPurchaseService).update(any());
    verify(installmentService).deleteByPurchase(purchase.getId());
    verify(installmentService).createList(any());
  }

  @Test
  @DisplayName("update de crédito recalcula mesmo quando a compra não tem parcelas registradas")
  void updateCreditWithPurchaseButNoInstallmentsRecalculates() {
    // Arrange
    UpdateTransactionRequest request = updateRequest(TransactionType.CREDIT, new BigDecimal("100"));
    InstallmentPurchase purchase = installmentPurchase();
    when(installmentPurchaseService.getByTransaction(request.id())).thenReturn(List.of(purchase));
    when(installmentService.getByPurchase(purchase.getId())).thenReturn(List.of());

    // Act
    service.update(request);

    // Assert
    verify(installmentService).createList(any());
  }

  // ----- delegação ao repositório -----

  @Test
  @DisplayName("get retorna a transação referenciada pelo id")
  void getReturnsReferencedTransaction() {
    // Arrange
    UUID id = UUID.randomUUID();
    Transaction transaction = new Transaction();
    when(repository.getReferenceById(id)).thenReturn(transaction);

    // Act
    Transaction result = service.get(id);

    // Assert
    assertThat(result).isSameAs(transaction);
  }

  @Test
  @DisplayName("getList retorna todas as transações")
  void getListReturnsAll() {
    // Arrange
    List<Transaction> transactions = List.of(new Transaction(), new Transaction());
    when(repository.findAll()).thenReturn(transactions);

    // Act
    List<Transaction> result = service.getList();

    // Assert
    assertThat(result).isEqualTo(transactions);
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
  @DisplayName("deleteAll remove todas as transações")
  void deleteAllRemovesEverything() {
    // Act
    service.deleteAll();

    // Assert
    verify(repository).deleteAll();
  }

  // ----- Helpers (sem estado mutável compartilhado entre testes) -----

  private static CreateTransactionRequestBuilder createRequestBuilder() {
    return new CreateTransactionRequestBuilder();
  }

  private static AccountCard accountCard() {
    AccountCard card = new AccountCard();
    card.setId(UUID.randomUUID());
    return card;
  }

  private static Category category() {
    Category category = new Category();
    category.setId(UUID.randomUUID());
    return category;
  }

  private static InstallmentPurchase installmentPurchase() {
    InstallmentPurchase purchase = new InstallmentPurchase();
    purchase.setId(UUID.randomUUID());
    return purchase;
  }

  private static Installment installment(int number, LocalDate dueDate) {
    Installment installment = new Installment();
    installment.setId(UUID.randomUUID());
    installment.setInstallmentNumber(number);
    installment.setDueDate(dueDate);
    return installment;
  }

  private static UpdateTransactionRequest updateRequest(TransactionType type, BigDecimal amount) {
    return new UpdateTransactionRequest(
        UUID.randomUUID(),
        new Account(),
        null,
        category(),
        type,
        amount,
        BigDecimal.ZERO,
        1,
        "Atualização",
        InstallmentType.PRICE);
  }

  /**
   * Builder local com defaults válidos; cada teste sobrescreve apenas o campo relevante,
   * mantendo o isolamento e deixando explícito o cenário (Arrange).
   */
  private static final class CreateTransactionRequestBuilder {
    private Account account = new Account();
    private AccountCard accountCard;
    private Category category = TransactionServiceTest.category();
    private TransactionType type = TransactionType.CASH;
    private BigDecimal amount = new BigDecimal("100");
    private BigDecimal fee = BigDecimal.ZERO;
    private Integer installment = 1;
    private String description = "Compra";
    private LocalDate dueDate = LocalDate.of(2026, Month.JANUARY, 10);
    private SubscriptionFrequency frequency;
    private InstallmentType installmentType = InstallmentType.PRICE;
    private Journal journal;

    CreateTransactionRequestBuilder accountCard(AccountCard v) { this.accountCard = v; return this; }
    CreateTransactionRequestBuilder category(Category v) { this.category = v; return this; }
    CreateTransactionRequestBuilder type(TransactionType v) { this.type = v; return this; }
    CreateTransactionRequestBuilder amount(BigDecimal v) { this.amount = v; return this; }
    CreateTransactionRequestBuilder fee(BigDecimal v) { this.fee = v; return this; }
    CreateTransactionRequestBuilder installment(Integer v) { this.installment = v; return this; }
    CreateTransactionRequestBuilder description(String v) { this.description = v; return this; }
    CreateTransactionRequestBuilder frequency(SubscriptionFrequency v) { this.frequency = v; return this; }
    CreateTransactionRequestBuilder installmentType(InstallmentType v) { this.installmentType = v; return this; }
    CreateTransactionRequestBuilder journal(Journal v) { this.journal = v; return this; }

    CreateTransactionRequest build() {
      return new CreateTransactionRequest(account, accountCard, category, type, amount, fee,
          installment, description, dueDate, frequency, installmentType, journal);
    }
  }
}
