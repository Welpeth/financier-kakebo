package com.welpeth.kakebo.financier.domain.installmentPurchase.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.CreateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.UpdateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import com.welpeth.kakebo.financier.domain.installmentPurchase.repository.InstallmentPurchaseRepository;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import java.math.BigDecimal;
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
class InstallmentPurchaseServiceTest {

  @Mock
  private InstallmentPurchaseRepository repository;

  @InjectMocks
  private InstallmentPurchaseService service;

  @Test
  @DisplayName("get retorna a compra parcelada quando encontrada")
  void getReturnsWhenFound() {
    // Arrange
    UUID id = UUID.randomUUID();
    InstallmentPurchase purchase = new InstallmentPurchase();
    when(repository.findById(id)).thenReturn(Optional.of(purchase));

    // Act
    InstallmentPurchase result = service.get(id);

    // Assert
    assertThat(result).isSameAs(purchase);
  }

  @Test
  @DisplayName("get lança 404 quando a compra parcelada não existe")
  void getThrowsWhenMissing() {
    // Arrange
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.empty());

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.get(id));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(ex.getReason()).isEqualTo("InstallmentPurchase não encontrada");
  }

  @Test
  @DisplayName("create gera id, mapeia os campos e persiste")
  void createMapsFieldsAndSaves() {
    // Arrange
    Transaction transaction = new Transaction();
    CreateInstallmentPurchaseRequest request = new CreateInstallmentPurchaseRequest(
        transaction, new BigDecimal("300"), 3, new BigDecimal("2.5"));
    when(repository.save(any(InstallmentPurchase.class))).thenAnswer(inv -> inv.getArgument(0));

    // Act
    InstallmentPurchase created = service.create(request);

    // Assert
    assertThat(created.getId()).isNotNull();
    assertThat(created.getTransaction()).isSameAs(transaction);
    assertThat(created.getTotalAmount()).isEqualByComparingTo("300");
    assertThat(created.getInterestRate()).isEqualByComparingTo("2.5");
  }

  @Test
  @DisplayName("getByTransaction delega ao repositório")
  void getByTransactionDelegates() {
    // Arrange
    UUID transactionId = UUID.randomUUID();
    List<InstallmentPurchase> purchases = List.of(new InstallmentPurchase());
    when(repository.findByTransactionId(transactionId)).thenReturn(purchases);

    // Act
    List<InstallmentPurchase> result = service.getByTransaction(transactionId);

    // Assert
    assertThat(result).isSameAs(purchases);
  }

  @Test
  @DisplayName("getList retorna todas as compras parceladas")
  void getListReturnsAll() {
    // Arrange
    List<InstallmentPurchase> purchases = List.of(new InstallmentPurchase());
    when(repository.findAll()).thenReturn(purchases);

    // Act
    List<InstallmentPurchase> result = service.getList();

    // Assert
    assertThat(result).isSameAs(purchases);
  }

  @Test
  @DisplayName("update delega ao repositório")
  void updateDelegatesToRepository() {
    // Arrange
    UpdateInstallmentPurchaseRequest request = new UpdateInstallmentPurchaseRequest(
        UUID.randomUUID(), new BigDecimal("300"), 3, new BigDecimal("2.5"));

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
}
