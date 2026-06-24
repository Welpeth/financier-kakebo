package com.welpeth.kakebo.financier.domain.transaction.service;

import com.welpeth.kakebo.financier.domain.accountCard.dto.AvailableLimitResponse;
import com.welpeth.kakebo.financier.domain.accountCard.service.AccountCardService;
import com.welpeth.kakebo.financier.domain.installment.dto.CreateInstallmentListRequest;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import com.welpeth.kakebo.financier.domain.installment.service.InstallmentService;
import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.CreateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.dto.UpdateInstallmentPurchaseRequest;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import com.welpeth.kakebo.financier.domain.installmentPurchase.service.InstallmentPurchaseService;
import com.welpeth.kakebo.financier.domain.ledgerEntry.dto.CreateLedgerEntryRequest;
import com.welpeth.kakebo.financier.domain.ledgerEntry.service.LedgerEntryService;
import com.welpeth.kakebo.financier.domain.subscription.dto.CreateSubscriptionRequest;
import com.welpeth.kakebo.financier.domain.subscription.service.SubscriptionService;
import com.welpeth.kakebo.financier.domain.transaction.dto.CreateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.dto.UpdateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.transaction.repository.TransactionRepository;
import com.welpeth.kakebo.financier.domain.transaction.type.TransactionType;
import com.welpeth.kakebo.financier.domain.subscription.type.SubscriptionFrequency;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
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
      private final AccountCardService accountCardService;
      private final LedgerEntryService ledgerEntryService;
    
      public Transaction get(UUID id) {
        return repository.getReferenceById(id);
      }
    
      public List<Transaction> getList() {
        return repository.findAll();
      }
    
      public Transaction create(CreateTransactionRequest request) {
        validateCreateRequest(request);
    
        validateCreditTransaction(request);
    
        Transaction saved = setAndSaveTransaction(request);
        createTransactionSchedule(saved, request);
        createLedgerEntry(saved, request.journal());
    
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
                transaction.getAmount(), transaction.getFee(), request.dueDate(),
                request.installmentType()));
      }
    
      @Transactional
      public void update(UpdateTransactionRequest request) {
        if (request.category() == null || request.category().getId() == null) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria obrigatória");
        }
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ONE) < 0) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              "Valor mínimo da transação é R$ 1,00");
        }
    
        repository.update(request);
    
        if (request.type() == TransactionType.CREDIT) {
          recalculateInstallments(request);
        }
      }
    
      private void recalculateInstallments(UpdateTransactionRequest request) {
        List<InstallmentPurchase> purchases = installmentPurchaseService.getByTransaction(request.id());
        if (purchases.isEmpty()) {
          return;
        }
    
        InstallmentPurchase purchase = purchases.get(0);
    
        LocalDate firstDueDate = installmentService.getByPurchase(purchase.getId()).stream()
            .min(Comparator.comparingInt(Installment::getInstallmentNumber))
            .map(Installment::getDueDate)
            .orElse(LocalDate.now());
    
        installmentPurchaseService.update(new UpdateInstallmentPurchaseRequest(
            purchase.getId(), request.amount(), request.installment(), request.fee()));
    
        installmentService.deleteByPurchase(purchase.getId());
    
        installmentService.createList(new CreateInstallmentListRequest(
            purchase, request.installment(), request.amount(), request.fee(), firstDueDate,
            request.installmentType()));
      }
    
      public void delete(UUID id) {
        repository.deleteById(id);
      }
    
      public void deleteAll() {
        repository.deleteAll();
      }
  
      public Transaction setAndSaveTransaction(CreateTransactionRequest request) {
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
            transaction.setInstallmentType(request.installmentType());
  
            return repository.save(transaction);
      }
    
      public void validateCreditTransaction(CreateTransactionRequest request) {
        if (isCreditTransaction(request.type())) {
          if (request.accountCard() != null) {
            BigDecimal totalToCommit = calculateTotalToCommit(request);
            AvailableLimitResponse limit = accountCardService.getAvailableLimit(
                request.accountCard().getId());
            if (totalToCommit.compareTo(limit.availableLimit()) > 0) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("Limite insuficiente. Disponível: R$ %.2f", limit.availableLimit()));
            }
          } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  "Cartão de crédito obrigatório para transações de crédito");
          }
        }
      }
  
    public void createTransactionSchedule(Transaction saved, CreateTransactionRequest request) {
      if (saved.isSubscription() && isCreditTransaction(type)) {
        subscriptionService.create(new CreateSubscriptionRequest(
            saved, request.frequency(), request.dueDate()
        ));
      } else {
        createInstallments(saved, request);
      }
    }

    public void validateCreateRequest(CreateTransactionRequest request) {
      if (request == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Dados obrigatórios"
        );
      }
      
      if (request.category() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria obrigatória");
      }
      
      if (request.amount().compareTo(BigDecimal.ONE) < 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Valor mínimo da transação é R$ 1,00");
      }
    }
    
    public BigDecimal calculateTotalToCommit(CreateTransactionRequest request) {
      if (request.frequency() != null) {
        return request.amount();
      } else {
        return installmentService.calculateTotalWithInterest(
                request.amount(),
                request.installment() != null ? request.installment() : 1,
                request.fee() != null ? request.fee() : BigDecimal.ZERO,
                request.installmentType());
        }
    }
  
    public void createLedgerEntry(Transaction saved, Journal journal) {
       if (journal != null) {
         ledgerEntryService.create(new CreateLedgerEntryRequest(
             saved.getDescription(), null, journal, saved
         ));
      }
    }
  
    private boolean isCreditTransaction(TransactionType type) {
      return type == TransactionType.CREDIT;
  }
}
