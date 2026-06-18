package com.welpeth.kakebo.financier.domain.notification.scheduler;

import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import com.welpeth.kakebo.financier.domain.installment.repository.InstallmentRepository;
import com.welpeth.kakebo.financier.domain.notification.service.NotificationService;
import com.welpeth.kakebo.financier.domain.notification.type.NotificationType;
import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import com.welpeth.kakebo.financier.domain.subscription.repository.SubscriptionRepository;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

  private static final int REMINDER_WINDOW_DAYS = 3;
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private final InstallmentRepository installmentRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final NotificationService notificationService;

  @Scheduled(cron = "0 0 7 * * *")
  @Transactional
  public void generateDueReminders() {
    LocalDate limit = LocalDate.now().plusDays(REMINDER_WINDOW_DAYS);

    List<Installment> installments = installmentRepository.findUpcomingUnpaid(limit);
    for (Installment installment : installments) {
      Transaction transaction = installment.getInstallmentPurchase().getTransaction();
      Holder holder = resolveHolder(transaction);
      if (holder == null) {
        continue;
      }

      notificationService.createIfAbsent(
          holder,
          NotificationType.INSTALLMENT_DUE,
          installment.getId(),
          "Parcela a vencer",
          String.format("Parcela %d vence em %s", installment.getInstallmentNumber(),
              installment.getDueDate().format(DATE_FORMAT)),
          installment.getAmount(),
          installment.getDueDate());
    }

    List<Subscription> subscriptions = subscriptionRepository.findUpcomingActive(limit);
    for (Subscription subscription : subscriptions) {
      Transaction transaction = subscription.getTransaction();
      Holder holder = resolveHolder(transaction);
      if (holder == null) {
        continue;
      }

      String description = transaction.getDescription() != null ? transaction.getDescription()
          : "Assinatura";

      notificationService.createIfAbsent(
          holder,
          NotificationType.SUBSCRIPTION_DUE,
          subscription.getId(),
          "Assinatura a cobrar",
          String.format("%s será cobrada em %s", description,
              subscription.getNextChargeDate().format(DATE_FORMAT)),
          transaction.getAmount(),
          subscription.getNextChargeDate());
    }
  }

  private Holder resolveHolder(Transaction transaction) {
    if (transaction == null) {
      return null;
    }
    if (transaction.getAccount() != null) {
      return transaction.getAccount().getHolder();
    }
    if (transaction.getAccountCard() != null) {
      Account account = transaction.getAccountCard().getAccount();
      return account != null ? account.getHolder() : null;
    }
    return null;
  }
}
