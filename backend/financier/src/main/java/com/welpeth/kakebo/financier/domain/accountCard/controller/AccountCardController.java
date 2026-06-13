package com.welpeth.kakebo.financier.domain.accountCard.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.accountCard.dto.CreateAccountCardRequest;
import com.welpeth.kakebo.financier.domain.accountCard.dto.UpdateAccountCardRequest;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import com.welpeth.kakebo.financier.domain.accountCard.service.AccountCardService;
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
@RequestMapping(ApiPath.ACCOUNT_CARD)
public class AccountCardController {

  private final AccountCardService accountCardService;

  @GetMapping("/{id}")
  public ResponseEntity<AccountCard> getById(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(accountCardService.get(id));
  }

  @GetMapping("/list")
  public ResponseEntity<List<AccountCard>> getList() {
    return ResponseEntity.status(HttpStatus.OK).body(accountCardService.getList());
  }

  @PostMapping
  public ResponseEntity<AccountCard> create(@RequestBody CreateAccountCardRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(accountCardService.create(request));
  }

  @PatchMapping
  public ResponseEntity<Void> update(@RequestBody UpdateAccountCardRequest request) {
    accountCardService.update(request);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    accountCardService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
