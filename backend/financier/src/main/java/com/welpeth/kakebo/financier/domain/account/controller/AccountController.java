package com.welpeth.kakebo.financier.domain.account.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.account.dto.CreateAccountRequest;
import com.welpeth.kakebo.financier.domain.account.dto.UpdateAccountRequest;
import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.account.service.AccountService;
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
@RequestMapping(ApiPath.ACCOUNT)
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @GetMapping("/{id}")
  public ResponseEntity<Account> getById(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(accountService.get(id));
  }

  @GetMapping("/list")
  public ResponseEntity<List<Account>> getList() {
    return ResponseEntity.status(HttpStatus.OK).body(accountService.getList());
  }

  @PostMapping
  public ResponseEntity<Account> create(@RequestBody CreateAccountRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(accountService.create(request));
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody UpdateAccountRequest request) {
    accountService.update(request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    accountService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
