package com.welpeth.kakebo.financier.domain.transaction.controller;


import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.transaction.dto.CreateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.dto.UpdateTransactionRequest;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import com.welpeth.kakebo.financier.domain.transaction.service.TransactionService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.TRANSACTION)
public class TransactionController {

  private final TransactionService transactionService;

  @GetMapping("/{id}")
  public ResponseEntity<Transaction> getById(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.get(id));
  }

  @GetMapping("/list")
  public ResponseEntity<List<Transaction>> getList() {
    return ResponseEntity.status(HttpStatus.OK).body(transactionService.getList());
  }

  @PostMapping
  public ResponseEntity<Transaction> create(@RequestBody CreateTransactionRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(request));
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody UpdateTransactionRequest request) {
    transactionService.update(request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    transactionService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteAll() {
    transactionService.deleteAll();
    return ResponseEntity.noContent().build();
  }
}
