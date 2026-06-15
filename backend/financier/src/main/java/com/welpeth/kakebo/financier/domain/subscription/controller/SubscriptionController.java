package com.welpeth.kakebo.financier.domain.subscription.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.subscription.dto.CreateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.dto.UpdateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.entity.Subscription;
import com.welpeth.kakebo.financier.domain.subscription.service.SubscriptionService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.SUBSCRIPTION)
public class SubscriptionController {

  private final SubscriptionService service;

  @GetMapping("/{id}")
  public ResponseEntity<Subscription> get(@PathVariable UUID id) {
    return ResponseEntity.ok(service.get(id));
  }

  @GetMapping("/list")
  public ResponseEntity<List<Subscription>> getList() {
    return ResponseEntity.ok(service.getList());
  }

  @GetMapping("/by-transaction/{transactionId}")
  public ResponseEntity<List<Subscription>> getByTransaction(@PathVariable UUID transactionId) {
    return ResponseEntity.ok(service.getByTransaction(transactionId));
  }

  @PostMapping
  public ResponseEntity<Subscription> create(@RequestBody CreateSubscriptionRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody UpdateSubscriptionRequest request) {
    service.update(request);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/pay")
  public ResponseEntity<Subscription> payCurrentPeriod(@PathVariable UUID id) {
    return ResponseEntity.ok(service.payCurrentPeriod(id));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
