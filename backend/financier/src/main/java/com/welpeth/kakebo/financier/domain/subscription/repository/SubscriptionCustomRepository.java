package com.welpeth.kakebo.financier.domain.subscription.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.subscription.dto.UpdateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

interface SubscriptionCustomRepository extends BaseCustomRepository<Subscription> {

  @Transactional
  void update(UpdateSubscriptionRequest request);

  BigDecimal sumActiveAmountByCardId(UUID cardId, LocalDate endOfMonth);

  List<Subscription> findUpcomingActive(LocalDate limit);
}
