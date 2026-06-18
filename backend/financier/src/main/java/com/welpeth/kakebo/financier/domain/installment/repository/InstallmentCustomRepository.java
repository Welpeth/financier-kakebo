package com.welpeth.kakebo.financier.domain.installment.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.installment.dto.UpdateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

interface InstallmentCustomRepository extends BaseCustomRepository<Installment> {

  @Transactional
  void update(UpdateInstallmentRequest request);

  BigDecimal sumUnpaidAmountByCardId(UUID cardId);

  List<Installment> findByCardId(UUID cardId);

  List<Installment> findUpcomingUnpaid(LocalDate limit);
}
