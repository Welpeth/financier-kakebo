package com.welpeth.kakebo.financier.domain.installment.service;

import com.welpeth.kakebo.financier.domain.installment.dto.CreateInstallmentListRequest;
import com.welpeth.kakebo.financier.domain.installment.dto.CreateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.dto.UpdateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import com.welpeth.kakebo.financier.domain.installment.repository.InstallmentRepository;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class InstallmentService {

  private final InstallmentRepository repository;

  public Installment get(UUID id) {
    return repository.findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Installment não encontrada"));
  }

  public List<Installment> getList() {
    return repository.findAll();
  }

  public List<Installment> getByPurchase(UUID installmentPurchaseId) {
    return repository.findByInstallmentPurchaseId(installmentPurchaseId);
  }

  public void createList(CreateInstallmentListRequest request) {
    List<Installment> installments = new ArrayList<>();

    for (int installmentCount = 0; installmentCount <= request.installmentNumber();
        installmentCount++) {
      Installment entity = new Installment();
      entity.setId(UUID.randomUUID());
      entity.setInstallmentPurchase(request.installmentPurchase());
      entity.setInstallmentNumber(request.installmentNumber());
      entity.setAmount(calculateInstallmentAmount(request.amount(), installmentCount,
          request.installmentNumber(), request.fee()));
      entity.setDueDate(request.dueDate());
      entity.setPaid(false);
      installments.add(entity);
    }
    repository.saveAll(installments);
  }

  public Installment create(CreateInstallmentRequest request) {
    Installment entity = new Installment();
    entity.setId(UUID.randomUUID());
    entity.setInstallmentPurchase(request.installmentPurchase());
    entity.setInstallmentNumber(request.installmentNumber());
    entity.setAmount(request.amount());
    entity.setDueDate(request.dueDate());
    entity.setPaid(false);
    return repository.save(entity);
  }

  public void update(UpdateInstallmentRequest request) {
    repository.update(request);
  }

  public void pay(UUID id) {
    Installment entity = get(id);
    entity.setPaid(true);
    entity.setPaidAt(LocalDate.now());
    repository.save(entity);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }

  private static BigDecimal calculateInstallmentAmount(BigDecimal totalAmount,
      int installmentCount, int totalInstallments, BigDecimal fee) {
    BigDecimal baseAmount = totalAmount.divide(BigDecimal.valueOf(totalInstallments),
        MathContext.DECIMAL64);
    BigDecimal currentFee = fee;
    for (int i = 1; i < installmentCount; i++) {
      currentFee = currentFee.multiply(fee);
    }
    return baseAmount.add(baseAmount.multiply(currentFee));
  }
}
