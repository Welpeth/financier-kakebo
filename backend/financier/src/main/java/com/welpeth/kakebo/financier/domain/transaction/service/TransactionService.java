package com.welpeth.kakebo.financier.domain.transaction.service;

import com.welpeth.kakebo.financier.domain.installment.dto.CreateInstallmentListRequest;
import com.welpeth.kakebo.financier.domain.installment.service.InstallmentService;
import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.CreateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import com.welpeth.kakebo.financier.domain.installmentPurchase.service.InstallmentPurchaseService;
import com.welpeth.kakebo.financier.domain.subscription.dto.CreateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.service.SubscriptionService;
import com.welpeth.kakebo.financier.domain.transaction.dto.CreateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.dto.UpdateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import com.welpeth.kakebo.financier.domain.transaction.repository.TransactionRepository;
import com.welpeth.kakebo.financier.domain.transaction.type.TransactionType;
import java.math.BigDecimal;
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
  private final InstallmentService installmentService;
  private final InstallmentPurchaseService installmentPurchaseService;
  private final SubscriptionService subscriptionService;

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
    if (request.amount() == null || request.amount().compareTo(BigDecimal.ONE) < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Valor mínimo da transação é R$ 1,00");
    }

    Transaction transaction = new Transaction();
    transaction.setId(UUID.randomUUID());
    transaction.setAccount(request.account());
    transaction.setAccountCard(request.accountCard());
    transaction.setCategory(request.category());
    transaction.setDescription(request.description());
    transaction.setAmount(request.amount());
    transaction.setFee(request.fee() != null ? request.fee() : BigDecimal.ZERO);
    transaction.setInstallment(request.installment());
    transaction.setType(request.type());
    transaction.setSubscription(request.frequency() != null);

    Transaction saved = repository.save(transaction);

    if (saved.getType() == TransactionType.CREDIT) {
      if (saved.isSubscription()) {
        subscriptionService.create(new CreateSubscriptionRequest(
            saved, request.frequency(), request.dueDate()
        ));
      } else {
        createInstallments(saved, request);
      }
    }

    return saved;
  }

  private void createInstallments(Transaction transaction, CreateTransactionRequest request) {
    InstallmentPurchase purchase = installmentPurchaseService.create(
        new CreateInstallmentPurchaseRequest(
            transaction,
            transaction.getAmount(),
            request.installment(),
            transaction.getFee()
        )
    );
    installmentService.createList(
        new CreateInstallmentListRequest(purchase, transaction.getInstallment(),
            transaction.getAmount(), transaction.getFee(), request.dueDate()));
  }

  public void update(UpdateTransactionRequest request) {
    if (request.category() == null || request.category().getId() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria obrigatória");
    }
    if (request.amount() == null || request.amount().compareTo(BigDecimal.ONE) < 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Valor mínimo da transação é R$ 1,00");
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
