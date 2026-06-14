package com.welpeth.kakebo.financier.domain.installmentPurchase.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.UpdateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import jakarta.transaction.Transactional;

interface InstallmentPurchaseCustomRepository extends BaseCustomRepository<InstallmentPurchase> {

  @Transactional
  void update(UpdateInstallmentPurchaseRequest request);
}
