package com.welpeth.kakebo.financier.domain.transaction.repository;

import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, TransactionCustomRepository {

}
