package com.welpeth.kakebo.financier.domain.account.service;

import com.welpeth.kakebo.financier.domain.account.dto.CreateAccountRequest;
import com.welpeth.kakebo.financier.domain.account.dto.UpdateAccountRequest;import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.account.repository.AccountRepository;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository repository;

  public Account create(CreateAccountRequest request) {
    Account account = new Account();
    account.setId(UUID.randomUUID());
    account.setName(request.name());
    account.setBalance(request.balance());
    account.setIsActive(request.isActive());
    account.setType(request.type());

    repository.save(account);
    return account;
  }

  public Account get(UUID id) {
    return repository.getReferenceById(id);
  }

  public void update(UpdateAccountRequest request) {
    repository.update(request);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }

  public List<Account> getList() {
    return repository.findAll();
  }
}
