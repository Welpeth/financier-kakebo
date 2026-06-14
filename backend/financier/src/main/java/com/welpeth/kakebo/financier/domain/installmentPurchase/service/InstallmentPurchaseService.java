package com.welpeth.kakebo.financier.domain.installmentPurchase.service;

import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.CreateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.UpdateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import com.welpeth.kakebo.financier.domain.installmentPurchase.repository.InstallmentPurchaseRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class InstallmentPurchaseService {

  private final InstallmentPurchaseRepository repository;

  public InstallmentPurchase get(UUID id) {
    return repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "InstallmentPurchase não encontrada"));
  }

  public List<InstallmentPurchase> getList() {
    return repository.findAll();
  }

  public List<InstallmentPurchase> getByTransaction(UUID transactionId) {
    return repository.findByTransactionId(transactionId);
  }

  public InstallmentPurchase create(CreateInstallmentPurchaseRequest request) {
    InstallmentPurchase entity = new InstallmentPurchase();
    entity.setId(UUID.randomUUID());
    entity.setTransaction(request.transaction());
    entity.setTotalAmount(request.totalAmount());
    entity.setInterestRate(request.interestRate());
    return repository.save(entity);
  }

  public void update(UpdateInstallmentPurchaseRequest request) {
    repository.update(request);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }
}
