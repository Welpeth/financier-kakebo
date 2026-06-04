package com.welpeth.kakebo.financier.domain.transaction.repository;

import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}
