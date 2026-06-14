package com.welpeth.kakebo.financier.domain.subscription.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.subscription.dto.UpdateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import jakarta.transaction.Transactional;

interface SubscriptionCustomRepository extends BaseCustomRepository<Subscription> {

  @Transactional
  void update(UpdateSubscriptionRequest request);
}
