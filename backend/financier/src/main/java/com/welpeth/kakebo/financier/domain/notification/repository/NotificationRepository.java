package com.welpeth.kakebo.financier.domain.notification.repository;

import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.notification.entity.Notification;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  List<Notification> findByHolderOrderByDueDateAsc(Holder holder);

  List<Notification> findByHolderAndReadFalse(Holder holder);

  long countByHolderAndReadFalse(Holder holder);

  boolean existsByReferenceIdAndDueDate(UUID referenceId, LocalDate dueDate);
}
