package com.welpeth.kakebo.financier.domain.installment.service;

import com.welpeth.kakebo.financier.domain.installment.dto.CreateInstallmentListRequest;
import com.welpeth.kakebo.financier.domain.installment.dto.CreateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.dto.UpdateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import com.welpeth.kakebo.financier.domain.installment.repository.InstallmentRepository;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import com.welpeth.kakebo.financier.domain.installmentPurchase.repository.InstallmentPurchaseRepository;
import com.welpeth.kakebo.financier.domain.transaction.type.InstallmentType;
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
  private final InstallmentPurchaseRepository installmentPurchaseRepository;

  public Installment get(UUID id) {
    return repository.findById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Installment não encontrada"));
  }

  public List<Installment> getList() {
    return repository.findAll();
  }

  public List<Installment> getByPurchase(UUID installmentPurchaseId) {
    return repository.findByInstallmentPurchaseId(installmentPurchaseId);
  }

  public List<Installment> getByCard(UUID cardId) {
    return repository.findByCardId(cardId);
  }

  public void createList(CreateInstallmentListRequest request) {
    int totalInstallments = request.installmentNumber();
    BigDecimal monthlyRate = request.fee().divide(BigDecimal.valueOf(100), MathContext.DECIMAL64);

    BigDecimal priceInstallment =
        request.installmentType() == InstallmentType.PRICE ? calculatePrice(request.amount(),
            totalInstallments, monthlyRate) : null;

    BigDecimal sacAmortization = request.installmentType() == InstallmentType.SAC ? request.amount()
        .divide(BigDecimal.valueOf(totalInstallments), MathContext.DECIMAL64) : null;

    List<Installment> installments = new ArrayList<>();
    for (int installmentNumber = 1; installmentNumber <= totalInstallments; installmentNumber++) {
      BigDecimal amount = request.installmentType() == InstallmentType.PRICE ? priceInstallment
          : sacAmortization.add(request.amount()
              .subtract(sacAmortization.multiply(BigDecimal.valueOf(installmentNumber - 1L)))
              .multiply(monthlyRate));

      installments.add(createInstallment(request.installmentPurchase(), installmentNumber, amount,
          request.dueDate().plusMonths(installmentNumber - 1L)));
    }
    repository.saveAll(installments);

    BigDecimal totalWithInterest = installments.stream().map(Installment::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    InstallmentPurchase purchase = request.installmentPurchase();
    purchase.setTotalAmountWithInterest(totalWithInterest);
    installmentPurchaseRepository.save(purchase);
  }

  private static BigDecimal calculatePrice(BigDecimal totalAmount, int totalInstallments,
      BigDecimal monthlyRate) {
    if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
      return totalAmount.divide(BigDecimal.valueOf(totalInstallments), MathContext.DECIMAL64);
    }
    BigDecimal onePlusRatePowN = BigDecimal.ONE.add(monthlyRate)
        .pow(totalInstallments, MathContext.DECIMAL64);
    BigDecimal numerator = totalAmount.multiply(monthlyRate).multiply(onePlusRatePowN);
    BigDecimal denominator = onePlusRatePowN.subtract(BigDecimal.ONE);
    return numerator.divide(denominator, MathContext.DECIMAL64);
  }

  public static BigDecimal calculateTotalWithInterest(BigDecimal amount, int totalInstallments,
      BigDecimal feePercent, InstallmentType installmentType) {
    BigDecimal monthlyRate = feePercent.divide(BigDecimal.valueOf(100), MathContext.DECIMAL64);
    if (installmentType == InstallmentType.PRICE) {
      BigDecimal pmt = calculatePrice(amount, totalInstallments, monthlyRate);
      return pmt.multiply(BigDecimal.valueOf(totalInstallments));
    }
    BigDecimal interestFactor = monthlyRate.multiply(BigDecimal.valueOf(totalInstallments + 1L))
        .divide(BigDecimal.valueOf(2), MathContext.DECIMAL64);
    return amount.multiply(BigDecimal.ONE.add(interestFactor));
  }

  public Installment create(CreateInstallmentRequest request) {
    return repository.save(
        createInstallment(request.installmentPurchase(), request.installmentNumber(),
            request.amount(), request.dueDate()));
  }

  public void update(UpdateInstallmentRequest request) {
    repository.update(request);
  }

  public void pay(UUID id) {
    Installment installment = get(id);
    installment.setPaid(true);
    installment.setPaidAt(LocalDate.now());
    repository.save(installment);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }

  public void deleteByPurchase(UUID installmentPurchaseId) {
    repository.deleteByInstallmentPurchaseId(installmentPurchaseId);
  }

  private Installment createInstallment(InstallmentPurchase purchase, int installmentNumber,
      BigDecimal amount, LocalDate dueDate) {
    Installment installment = new Installment();
    installment.setId(UUID.randomUUID());
    installment.setInstallmentPurchase(purchase);
    installment.setInstallmentNumber(installmentNumber);
    installment.setAmount(amount);
    installment.setDueDate(dueDate);
    installment.setPaid(false);
    return installment;
  }
}
