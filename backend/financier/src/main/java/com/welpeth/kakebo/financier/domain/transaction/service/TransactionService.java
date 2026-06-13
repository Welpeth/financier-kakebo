package com.welpeth.kakebo.financier.domain.transaction.service;

import com.welpeth.kakebo.financier.domain.transaction.dto.CreateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.dto.UpdateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import com.welpeth.kakebo.financier.domain.transaction.repository.TransactionRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    if (request.category() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria obrigatória");
    }
    if (request.amount() == null || request.amount().compareTo(java.math.BigDecimal.ONE) < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor mínimo da transação é R$ 1,00");
    }

    Transaction transaction = new Transaction();
    transaction.setId(UUID.randomUUID());
    transaction.setAccount(request.account());
    transaction.setAccountCard(request.accountCard());
    transaction.setCategory(request.category());
    transaction.setDescription(request.description());
    transaction.setAmount(request.amount());
    transaction.setFee(request.fee() != null ? request.fee() : java.math.BigDecimal.ZERO);
    transaction.setInstallment(request.installment());
    transaction.setType(request.type());

    return repository.save(transaction);
  }

  public void update(UpdateTransactionRequest request) {
    if (request.category() == null || request.category().getId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria obrigatória");
    }
    if (request.amount() == null || request.amount().compareTo(java.math.BigDecimal.ONE) < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor mínimo da transação é R$ 1,00");
    }
    repository.update(request);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }

  public void deleteAll() {
    repository.deleteAll();
  }
}
