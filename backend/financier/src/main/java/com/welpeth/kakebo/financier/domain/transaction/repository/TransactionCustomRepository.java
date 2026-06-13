package com.welpeth.kakebo.financier.domain.transaction.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.transaction.dto.UpdateTransactionRequest;import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;import jakarta.transaction.Transactional;

public interface TransactionCustomRepository extends BaseCustomRepository<Transaction> {

@Transactional void update(UpdateTransactionRequest request);}
