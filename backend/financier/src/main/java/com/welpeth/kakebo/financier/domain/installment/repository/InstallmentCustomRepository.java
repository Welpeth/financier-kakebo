package com.welpeth.kakebo.financier.domain.installment.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.installment.dto.UpdateInstallmentRequest;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import jakarta.transaction.Transactional;

interface InstallmentCustomRepository extends BaseCustomRepository<Installment> {

  @Transactional
  void update(UpdateInstallmentRequest request);
}
