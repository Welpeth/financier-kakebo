package com.welpeth.kakebo.financier.domain.transaction.service;

import com.welpeth.kakebo.financier.domain.transaction.dto.CreateTransactionRequest;import com.welpeth.kakebo.financier.domain.transaction.dto.UpdateTransactionRequest;import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import com.welpeth.kakebo.financier.domain.transaction.repository.TransactionRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository repository;

  public Transaction get(UUID id) {
    return repository.getReferenceById(id);
  }

  public List<Transaction> getList() {
    return repository.findAll();
  }

  public Transaction create(CreateTransactionRequest request) {
    Transaction transaction = new Transaction();
    transaction.setId(UUID.randomUUID());
    transaction.setAccount(request.account());
    transaction.setAccountCard(request.accountCard());
    transaction.setDescription(request.description());
    transaction.setFee(request.fee());
    transaction.setInstallment(request.installment());
    transaction.setType(request.type());

    return repository.save(transaction);
  }

  public void update(UpdateTransactionRequest request) {
    repository.update(request);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }

  public void deleteAll() {
    repository.deleteAll();
  }
}
