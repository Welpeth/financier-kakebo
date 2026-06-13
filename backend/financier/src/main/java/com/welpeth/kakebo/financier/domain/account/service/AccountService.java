package com.welpeth.kakebo.financier.domain.account.service;

import com.welpeth.kakebo.financier.domain.account.dto.CreateAccountRequest;
import com.welpeth.kakebo.financier.domain.account.dto.UpdateAccountRequest;
import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.account.repository.AccountRepository;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.holder.repository.HolderRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository repository;
  private final HolderRepository holderRepository;

  public Account create(CreateAccountRequest request) {
    String email = (String) Objects.requireNonNull(
        SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    Holder holder = holderRepository.findUserByEmail(email)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

    Account account = new Account();
    account.setId(UUID.randomUUID());
    account.setName(request.name());
    account.setBalance(request.balance());
    account.setIsActive(request.isActive());
    account.setType(request.type());
    account.setHolder(holder);

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
    String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Holder holder = holderRepository.findUserByEmail(email)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
    return repository.findByHolder(holder);
  }
}
