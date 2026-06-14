package com.welpeth.kakebo.financier.domain.installment.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.installment.dto.CreateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.dto.UpdateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import com.welpeth.kakebo.financier.domain.installment.service.InstallmentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.INSTALLMENT)
public class InstallmentController {

  private final InstallmentService service;

  @GetMapping("/{id}")
  public ResponseEntity<Installment> get(@PathVariable UUID id) {
    return ResponseEntity.ok(service.get(id));
  }

  @GetMapping("/list")
  public ResponseEntity<List<Installment>> getList() {
    return ResponseEntity.ok(service.getList());
  }

  @GetMapping("/by-purchase/{installmentPurchaseId}")
  public ResponseEntity<List<Installment>> getByPurchase(@PathVariable UUID installmentPurchaseId) {
    return ResponseEntity.ok(service.getByPurchase(installmentPurchaseId));
  }

  @PostMapping
  public ResponseEntity<Installment> create(@RequestBody CreateInstallmentRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody UpdateInstallmentRequest request) {
    service.update(request);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}/pay")
  public ResponseEntity<Void> pay(@PathVariable UUID id) {
    service.pay(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
