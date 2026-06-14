package com.welpeth.kakebo.financier.domain.installmentPurchase.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.CreateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.UpdateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import com.welpeth.kakebo.financier.domain.installmentPurchase.service.InstallmentPurchaseService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.INSTALLMENT_PURCHASE)
public class InstallmentPurchaseController {

  private final InstallmentPurchaseService service;

  @GetMapping("/{id}")
  public ResponseEntity<InstallmentPurchase> get(@PathVariable UUID id) {
    return ResponseEntity.ok(service.get(id));
  }

  @GetMapping("/list")
  public ResponseEntity<List<InstallmentPurchase>> getList() {
    return ResponseEntity.ok(service.getList());
  }

  @GetMapping("/by-transaction/{transactionId}")
  public ResponseEntity<List<InstallmentPurchase>> getByTransaction(@PathVariable UUID transactionId) {
    return ResponseEntity.ok(service.getByTransaction(transactionId));
  }

  @PostMapping
  public ResponseEntity<InstallmentPurchase> create(@RequestBody CreateInstallmentPurchaseRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody UpdateInstallmentPurchaseRequest request) {
    service.update(request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
