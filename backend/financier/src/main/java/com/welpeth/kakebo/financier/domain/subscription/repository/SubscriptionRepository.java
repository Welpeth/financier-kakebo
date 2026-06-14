package com.welpeth.kakebo.financier.domain.subscription.repository;

import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID>,
    SubscriptionCustomRepository {

  List<Subscription> findByTransactionId(UUID transactionId);

  List<Subscription> findAllByActiveTrueAndNextChargeDateLessThanEqual(LocalDate date);
}
