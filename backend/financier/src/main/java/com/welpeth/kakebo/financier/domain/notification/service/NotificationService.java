package com.welpeth.kakebo.financier.domain.notification.service;

import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.holder.repository.HolderRepository;
import com.welpeth.kakebo.financier.domain.notification.entity.Notification;
import com.welpeth.kakebo.financier.domain.notification.repository.NotificationRepository;
import com.welpeth.kakebo.financier.domain.notification.type.NotificationType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository repository;
  private final HolderRepository holderRepository;

  public List<Notification> getList() {
    return repository.findByHolderOrderByDueDateAsc(getCurrentHolder());
  }

  public long unreadCount() {
    return repository.countByHolderAndReadFalse(getCurrentHolder());
  }

  public void markAsRead(UUID id) {
    Holder holder = getCurrentHolder();
    Notification notification = repository.findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));

    if (!notification.getHolder().getId().equals(holder.getId())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Notificação não pertence ao usuário");
    }

    notification.setRead(true);
    repository.save(notification);
  }

  public void markAllAsRead() {
    List<Notification> unread = repository.findByHolderAndReadFalse(getCurrentHolder());
    unread.forEach(notification -> notification.setRead(true));
    repository.saveAll(unread);
  }

  public void createIfAbsent(Holder holder, NotificationType type, UUID referenceId, String title,
      String message, BigDecimal amount, LocalDate dueDate) {
    if (repository.existsByReferenceIdAndDueDate(referenceId, dueDate)) {
      return;
    }

    Notification notification = new Notification();
    notification.setId(UUID.randomUUID());
    notification.setHolder(holder);
    notification.setType(type);
    notification.setReferenceId(referenceId);
    notification.setTitle(title);
    notification.setMessage(message);
    notification.setAmount(amount);
    notification.setDueDate(dueDate);
    notification.setRead(false);

    repository.save(notification);
  }

  private Holder getCurrentHolder() {
    String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return holderRepository.findUserByEmail(email)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
  }
}
