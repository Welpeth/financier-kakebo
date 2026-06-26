package com.welpeth.kakebo.financier.domain.installment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.installment.dto.CreateInstallmentListRequest;
import com.welpeth.kakebo.financier.domain.installment.dto.CreateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.dto.UpdateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import com.welpeth.kakebo.financier.domain.installment.repository.InstallmentRepository;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import com.welpeth.kakebo.financier.domain.installmentPurchase.repository.InstallmentPurchaseRepository;
import com.welpeth.kakebo.financier.domain.transaction.type.InstallmentType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class InstallmentServiceTest {

  @Mock
  private InstallmentRepository repository;
  @Mock
  private InstallmentPurchaseRepository installmentPurchaseRepository;

  @InjectMocks
  private InstallmentService service;

  // ----- calculateTotalWithInterest (static) -----

  @Test
  @DisplayName("calculateTotalWithInterest sem juros retorna o próprio principal")
  void calculateTotalWithoutFeeEqualsPrincipal() {
    // Arrange
    BigDecimal amount = new BigDecimal("300");

    // Act
    BigDecimal total = InstallmentService.calculateTotalWithInterest(
        amount, 3, BigDecimal.ZERO, InstallmentType.PRICE);

    // Assert
    assertThat(total).isEqualByComparingTo("300");
  }

  @Test
  @DisplayName("calculateTotalWithInterest PRICE com juros retorna valor acima do principal")
  void calculateTotalPriceWithFeeGreaterThanPrincipal() {
    // Arrange
    BigDecimal amount = new BigDecimal("300");

    // Act
    BigDecimal total = InstallmentService.calculateTotalWithInterest(
        amount, 3, new BigDecimal("2"), InstallmentType.PRICE);

    // Assert
    assertThat(total).isGreaterThan(amount);
  }

  @Test
  @DisplayName("calculateTotalWithInterest SAC com juros retorna valor acima do principal")
  void calculateTotalSacWithFeeGreaterThanPrincipal() {
    // Arrange
    BigDecimal amount = new BigDecimal("300");

    // Act
    BigDecimal total = InstallmentService.calculateTotalWithInterest(
        amount, 3, new BigDecimal("2"), InstallmentType.SAC);

    // Assert
    assertThat(total).isGreaterThan(amount);
  }

  // ----- createList -----

  @Test
  @DisplayName("createList PRICE gera a quantidade de parcelas pedida e atualiza o total da compra")
  void createListPriceCreatesInstallmentsAndUpdatesPurchase() {
    // Arrange
    InstallmentPurchase purchase = installmentPurchase();
    CreateInstallmentListRequest request = new CreateInstallmentListRequest(
        purchase, 3, new BigDecimal("300"), new BigDecimal("2"),
        LocalDate.of(2026, Month.JANUARY, 10), InstallmentType.PRICE);

    // Act
    service.createList(request);

    // Assert
    List<Installment> saved = captureSavedInstallments();
    assertThat(saved).hasSize(3);
    assertThat(purchase.getTotalAmountWithInterest()).isNotNull();
    verify(installmentPurchaseRepository).save(purchase);
  }

  @Test
  @DisplayName("createList SAC gera a quantidade de parcelas pedida e atualiza o total da compra")
  void createListSacCreatesInstallmentsAndUpdatesPurchase() {
    // Arrange
    InstallmentPurchase purchase = installmentPurchase();
    CreateInstallmentListRequest request = new CreateInstallmentListRequest(
        purchase, 4, new BigDecimal("400"), new BigDecimal("1.5"),
        LocalDate.of(2026, Month.JANUARY, 10), InstallmentType.SAC);

    // Act
    service.createList(request);

    // Assert
    List<Installment> saved = captureSavedInstallments();
    assertThat(saved).hasSize(4);
    assertThat(saved).allSatisfy(installment -> assertThat(installment.getPaid()).isFalse());
    verify(installmentPurchaseRepository).save(purchase);
  }

  @Test
  @DisplayName("createList numera as parcelas sequencialmente e avança o vencimento mês a mês")
  void createListNumbersInstallmentsAndAdvancesDueDate() {
    // Arrange
    InstallmentPurchase purchase = installmentPurchase();
    LocalDate firstDueDate = LocalDate.of(2026, Month.JANUARY, 10);
    CreateInstallmentListRequest request = new CreateInstallmentListRequest(
        purchase, 2, new BigDecimal("200"), BigDecimal.ZERO, firstDueDate, InstallmentType.PRICE);

    // Act
    service.createList(request);

    // Assert
    List<Installment> saved = captureSavedInstallments();
    assertThat(saved.get(0).getInstallmentNumber()).isEqualTo(1);
    assertThat(saved.get(0).getDueDate()).isEqualTo(firstDueDate);
    assertThat(saved.get(1).getInstallmentNumber()).isEqualTo(2);
    assertThat(saved.get(1).getDueDate()).isEqualTo(firstDueDate.plusMonths(1));
  }

  // ----- pay -----

  @Test
  @DisplayName("pay marca a parcela como paga e registra a data de pagamento")
  void payMarksInstallmentPaid() {
    // Arrange
    UUID id = UUID.randomUUID();
    Installment installment = new Installment();
    installment.setPaid(false);
    when(repository.findById(id)).thenReturn(Optional.of(installment));

    // Act
    service.pay(id);

    // Assert
    assertThat(installment.getPaid()).isTrue();
    assertThat(installment.getPaidAt()).isEqualTo(LocalDate.now());
    verify(repository).save(installment);
  }

  // ----- get -----

  @Test
  @DisplayName("get retorna a parcela quando encontrada")
  void getReturnsWhenFound() {
    // Arrange
    UUID id = UUID.randomUUID();
    Installment installment = new Installment();
    when(repository.findById(id)).thenReturn(Optional.of(installment));

    // Act
    Installment result = service.get(id);

    // Assert
    assertThat(result).isSameAs(installment);
  }

  @Test
  @DisplayName("get lança 404 quando a parcela não existe")
  void getThrowsWhenMissing() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.empty());

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.get(id));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(ex.getReason()).isEqualTo("Installment não encontrada");
  }

  // ----- create / delegação -----

  @Test
  @DisplayName("create persiste uma parcela mapeada a partir do request")
  void createMapsFieldsAndSaves() {
    // Arrange
    InstallmentPurchase purchase = installmentPurchase();
    CreateInstallmentRequest request = new CreateInstallmentRequest(
        purchase, 1, new BigDecimal("100"), LocalDate.of(2026, Month.JANUARY, 10));
    when(repository.save(any(Installment.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    Installment created = service.create(request);

    // Assert
    assertThat(created.getId()).isNotNull();
    assertThat(created.getInstallmentNumber()).isEqualTo(1);
    assertThat(created.getAmount()).isEqualByComparingTo("100");
    assertThat(created.getInstallmentPurchase()).isSameAs(purchase);
    assertThat(created.getPaid()).isFalse();
  }

  @Test
  @DisplayName("getByPurchase delega ao repositório")
  void getByPurchaseDelegates() {
    // Arrange
    UUID purchaseId = UUID.randomUUID();
    List<Installment> installments = List.of(new Installment());
    when(repository.findByInstallmentPurchaseId(purchaseId)).thenReturn(installments);

    // Act
    List<Installment> result = service.getByPurchase(purchaseId);

    // Assert
    assertThat(result).isSameAs(installments);
  }

  @Test
  @DisplayName("getByCard delega ao repositório")
  void getByCardDelegates() {
    // Arrange
    UUID cardId = UUID.randomUUID();
    List<Installment> installments = List.of(new Installment());
    when(repository.findByCardId(cardId)).thenReturn(installments);

    // Act
    List<Installment> result = service.getByCard(cardId);

    // Assert
    assertThat(result).isSameAs(installments);
  }

  @Test
  @DisplayName("getList retorna todas as parcelas")
  void getListReturnsAll() {
    // Arrange
    List<Installment> installments = List.of(new Installment(), new Installment());
    when(repository.findAll()).thenReturn(installments);

    // Act
    List<Installment> result = service.getList();

    // Assert
    assertThat(result).isSameAs(installments);
  }

  @Test
  @DisplayName("update delega ao repositório")
  void updateDelegatesToRepository() {
    // Arrange
    UpdateInstallmentRequest request = new UpdateInstallmentRequest(
        UUID.randomUUID(), new BigDecimal("100"), LocalDate.now(), false, null);

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
  @DisplayName("deleteByPurchase delega ao repositório com o id da compra")
  void deleteByPurchaseDelegates() {
    // Arrange
    UUID purchaseId = UUID.randomUUID();

    // Act
    service.deleteByPurchase(purchaseId);

    // Assert
    verify(repository).deleteByInstallmentPurchaseId(purchaseId);
  }

  private List<Installment> captureSavedInstallments() {
    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<Installment>> captor = ArgumentCaptor.forClass(List.class);
    verify(repository).saveAll(captor.capture());
    return captor.getValue();
  }

  private static InstallmentPurchase installmentPurchase() {
    InstallmentPurchase purchase = new InstallmentPurchase();
    purchase.setId(UUID.randomUUID());
    return purchase;
  }
}
