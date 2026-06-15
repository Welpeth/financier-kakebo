package com.welpeth.kakebo.financier.domain.installment.repository;

import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, UUID>,
    InstallmentCustomRepository {

  List<Installment> findByInstallmentPurchaseId(UUID installmentPurchaseId);

  void deleteByInstallmentPurchaseId(UUID installmentPurchaseId);
}
