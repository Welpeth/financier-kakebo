package com.welpeth.kakebo.financier.domain.subscription.service;

import com.welpeth.kakebo.financier.domain.subscription.dto.CreateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.dto.UpdateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import com.welpeth.kakebo.financier.domain.subscription.repository.SubscriptionRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository repository;

  public Subscription get(UUID id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription não encontrada"));
  }

  public List<Subscription> getList() {
    return repository.findAll();
  }

  public List<Subscription> getByTransaction(UUID transactionId) {
    return repository.findByTransactionId(transactionId);
  }

  public Subscription create(CreateSubscriptionRequest request) {
    Subscription entity = new Subscription();
    entity.setId(UUID.randomUUID());
    entity.setTransaction(request.transaction());
    entity.setFrequency(request.frequency());
    entity.setNextChargeDate(request.nextChargeDate());
    entity.setActive(true);
    return repository.save(entity);
  }

  public void update(UpdateSubscriptionRequest request) {
    repository.update(request);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }
}
