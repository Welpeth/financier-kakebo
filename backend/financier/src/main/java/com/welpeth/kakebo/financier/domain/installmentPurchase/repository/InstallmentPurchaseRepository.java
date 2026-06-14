package com.welpeth.kakebo.financier.domain.installmentPurchase.repository;

import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentPurchaseRepository extends JpaRepository<InstallmentPurchase, UUID>,
    InstallmentPurchaseCustomRepository {

  List<InstallmentPurchase> findByTransactionId(UUID transactionId);
}
