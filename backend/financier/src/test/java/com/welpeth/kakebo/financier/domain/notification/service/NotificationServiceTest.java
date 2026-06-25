package com.welpeth.kakebo.financier.domain.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.holder.repository.HolderRepository;
import com.welpeth.kakebo.financier.domain.notification.entity.Notification;
import com.welpeth.kakebo.financier.domain.notification.repository.NotificationRepository;
import com.welpeth.kakebo.financier.domain.notification.type.NotificationType;
import java.math.BigDecimal;
import java.time.LocalDate;
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
class NotificationServiceTest {

  private static final String EMAIL = "henrique@example.com";

  @Mock
  private NotificationRepository repository;
  @Mock
  private HolderRepository holderRepository;

  @InjectMocks
  private NotificationService service;

  @AfterEach
  void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("getList retorna as notificações do holder autenticado ordenadas por vencimento")
  void getListReturnsHolderNotifications() {
    // Arrange
    Holder holder = authenticateHolder();
    List<Notification> notifications = List.of(new Notification(), new Notification());
    when(repository.findByHolderOrderByDueDateAsc(holder)).thenReturn(notifications);

    // Act
    List<Notification> result = service.getList();

    // Assert
    assertThat(result).isSameAs(notifications);
  }

  @Test
  @DisplayName("unreadCount retorna a contagem de não lidas do holder autenticado")
  void unreadCountDelegates() {
    // Arrange
    Holder holder = authenticateHolder();
    when(repository.countByHolderAndReadFalse(holder)).thenReturn(3L);

    // Act
    long result = service.unreadCount();

    // Assert
    assertThat(result).isEqualTo(3L);
  }

  @Test
  @DisplayName("markAsRead marca a notificação como lida quando pertence ao holder")
  void markAsReadMarksOwnNotification() {
    // Arrange
    Holder holder = authenticateHolder();
    Notification notification = new Notification();
    notification.setHolder(holder);
    notification.setRead(false);
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.of(notification));

    // Act
    service.markAsRead(id);

    // Assert
    assertThat(notification.getRead()).isTrue();
    verify(repository).save(notification);
  }

  @Test
  @DisplayName("markAsRead lança 404 quando a notificação não existe")
  void markAsReadThrowsWhenMissing() {
    // Arrange
    authenticateHolder();
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.empty());

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.markAsRead(id));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("markAsRead lança 403 quando a notificação pertence a outro holder")
  void markAsReadThrowsWhenNotOwner() {
    // Arrange
    authenticateHolder();
    Holder otherHolder = new Holder();
    otherHolder.setId(UUID.randomUUID());
    Notification notification = new Notification();
    notification.setHolder(otherHolder);
    UUID id = UUID.randomUUID();
    when(repository.findById(id)).thenReturn(Optional.of(notification));

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.markAsRead(id));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("markAllAsRead marca todas as não lidas do holder autenticado")
  void markAllAsReadMarksEveryUnread() {
    // Arrange
    Holder holder = authenticateHolder();
    Notification first = new Notification();
    first.setRead(false);
    Notification second = new Notification();
    second.setRead(false);
    List<Notification> unread = List.of(first, second);
    when(repository.findByHolderAndReadFalse(holder)).thenReturn(unread);

    // Act
    service.markAllAsRead();

    // Assert
    assertThat(first.getRead()).isTrue();
    assertThat(second.getRead()).isTrue();
    verify(repository).saveAll(unread);
  }

  @Test
  @DisplayName("createIfAbsent não cria quando já existe notificação para referência e vencimento")
  void createIfAbsentSkipsWhenPresent() {
    // Arrange
    Holder holder = new Holder();
    UUID referenceId = UUID.randomUUID();
    LocalDate dueDate = LocalDate.of(2026, 7, 10);
    when(repository.existsByReferenceIdAndDueDate(referenceId, dueDate)).thenReturn(true);

    // Act
    service.createIfAbsent(holder, NotificationType.INSTALLMENT_DUE, referenceId, "Título",
        "Mensagem", new BigDecimal("100"), dueDate);

    // Assert
    verify(repository, never()).save(any());
  }

  @Test
  @DisplayName("createIfAbsent cria e persiste a notificação quando ainda não existe")
  void createIfAbsentCreatesWhenAbsent() {
    // Arrange
    Holder holder = new Holder();
    UUID referenceId = UUID.randomUUID();
    LocalDate dueDate = LocalDate.of(2026, 7, 10);
    when(repository.existsByReferenceIdAndDueDate(referenceId, dueDate)).thenReturn(false);

    // Act
    service.createIfAbsent(holder, NotificationType.SUBSCRIPTION_DUE, referenceId, "Título",
        "Mensagem", new BigDecimal("100"), dueDate);

    // Assert
    verify(repository).save(any(Notification.class));
  }

  private Holder authenticateHolder() {
    Holder holder = new Holder();
    holder.setId(UUID.randomUUID());
    SecurityContextHolder.setContext(new SecurityContextImpl(
        new UsernamePasswordAuthenticationToken(EMAIL, null)));
    when(holderRepository.findUserByEmail(EMAIL)).thenReturn(Optional.of(holder));
    return holder;
  }
}
