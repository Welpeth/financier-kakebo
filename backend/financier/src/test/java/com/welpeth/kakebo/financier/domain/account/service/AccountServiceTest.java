package com.welpeth.kakebo.financier.domain.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.account.dto.CreateAccountRequest;
import com.welpeth.kakebo.financier.domain.account.dto.UpdateAccountRequest;
import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.account.repository.AccountRepository;
import com.welpeth.kakebo.financier.domain.account.type.AccountType;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.holder.repository.HolderRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  private static final String EMAIL = "henrique@example.com";

  @Mock
  private AccountRepository repository;
  @Mock
  private HolderRepository holderRepository;

  @InjectMocks
  private AccountService service;

  @AfterEach
  void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("create mapeia o request, associa o holder autenticado e persiste")
  void createMapsFieldsAndAssignsHolder() {
    // Arrange
    authenticateAs(EMAIL);
    Holder holder = new Holder();
    holder.setId(UUID.randomUUID());
    when(holderRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(holder));
    CreateAccountRequest request =
        new CreateAccountRequest("Conta Corrente", true, new BigDecimal("100"), AccountType.CHECKING);

    // Act
    Account created = service.create(request);

    // Assert
    assertThat(created.getId()).isNotNull();
    assertThat(created.getName()).isEqualTo("Conta Corrente");
    assertThat(created.getBalance()).isEqualByComparingTo("100");
    assertThat(created.getIsActive()).isTrue();
    assertThat(created.getType()).isEqualTo(AccountType.CHECKING);
    assertThat(created.getHolder()).isSameAs(holder);
    verify(repository).save(created);
  }

  @Test
  @DisplayName("create lança 401 quando o holder autenticado não existe")
  void createThrowsWhenHolderMissing() {
    // Arrange
    authenticateAs(EMAIL);
    when(holderRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());
    CreateAccountRequest request =
        new CreateAccountRequest("Conta", true, BigDecimal.TEN, AccountType.SAVINGS);

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.create(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(ex.getReason()).isEqualTo("Usuário não encontrado");
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("getList retorna as contas do holder autenticado")
  void getListReturnsHolderAccounts() {
    // Arrange
    authenticateAs(EMAIL);
    Holder holder = new Holder();
    holder.setId(UUID.randomUUID());
    when(holderRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(holder));
    List<Account> accounts = List.of(new Account(), new Account());
    when(repository.findByHolder(holder)).thenReturn(accounts);

    // Act
    List<Account> result = service.getList();

    // Assert
    assertThat(result).isSameAs(accounts);
  }

  @Test
  @DisplayName("getList lança 401 quando o holder autenticado não existe")
  void getListThrowsWhenHolderMissing() {
    // Arrange
    authenticateAs(EMAIL);
    when(holderRepository.findUserByEmail(EMAIL)).thenReturn(Optional.empty());

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.getList());

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  @Test
  @DisplayName("get retorna a conta referenciada pelo id")
  void getReturnsReferencedAccount() {
    // Arrange
    UUID id = UUID.randomUUID();
    Account account = new Account();
    when(repository.getReferenceById(id)).thenReturn(account);

    // Act
    Account result = service.get(id);

    // Assert
    assertThat(result).isSameAs(account);
  }

  @Test
  @DisplayName("update delega ao repositório")
  void updateDelegatesToRepository() {
    // Arrange
    UpdateAccountRequest request = new UpdateAccountRequest(
        UUID.randomUUID(), "Nova", false, BigDecimal.ZERO, AccountType.CHECKING);

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

  private static void authenticateAs(String email) {
    SecurityContextHolder.setContext(new SecurityContextImpl(
        new UsernamePasswordAuthenticationToken(email, null)));
  }
}
