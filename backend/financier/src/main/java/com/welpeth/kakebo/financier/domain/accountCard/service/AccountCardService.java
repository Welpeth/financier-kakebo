package com.welpeth.kakebo.financier.domain.accountCard.service;

import com.welpeth.kakebo.financier.domain.accountCard.dto.CreateAccountCardRequest;
import com.welpeth.kakebo.financier.domain.accountCard.dto.UpdateAccountCardRequest;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import com.welpeth.kakebo.financier.domain.accountCard.repository.AccountCardRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountCardService {

  private final AccountCardRepository repository;

  public AccountCard create(CreateAccountCardRequest request) {
    AccountCard accountCard = new AccountCard();
    accountCard.setId(UUID.randomUUID());
    accountCard.setName(request.name());
    accountCard.setIsActive(request.isActive());
    accountCard.setType(request.type());
    accountCard.setCreditLimit(request.creditLimit());
    accountCard.setExpirationMonth(request.expirationMonth());
    accountCard.setExpirationYear(request.expirationYear());
    accountCard.setAccount(request.account());

    repository.save(accountCard);
    return accountCard;
  }

  public AccountCard get(UUID id) {
    return repository.getReferenceById(id);
  }

  public void update(UpdateAccountCardRequest request) {
    repository.update(request);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }

  public List<AccountCard> getList() {
    return repository.findAll();
  }

  public List<AccountCard> getListByAccount(UUID accountId) {
    return repository.findByAccountId(accountId);
  }
}
