package com.welpeth.kakebo.financier.domain.accountCard.service;

import com.welpeth.kakebo.financier.domain.accountCard.dto.AvailableLimitResponse;
import com.welpeth.kakebo.financier.domain.accountCard.dto.CreateAccountCardRequest;
import com.welpeth.kakebo.financier.domain.accountCard.dto.UpdateAccountCardRequest;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import com.welpeth.kakebo.financier.domain.accountCard.repository.AccountCardRepository;
import com.welpeth.kakebo.financier.domain.installment.repository.InstallmentRepository;
import com.welpeth.kakebo.financier.domain.subscription.repository.SubscriptionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
@RequiredArgsConstructor
public class AccountCardService {

  private final AccountCardRepository repository;
  private final InstallmentRepository installmentRepository;
  private final SubscriptionRepository subscriptionRepository;

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

  public AvailableLimitResponse getAvailableLimit(UUID cardId) {
    AccountCard card = get(cardId);
    BigDecimal creditLimit = card.getCreditLimit() != null ? card.getCreditLimit() : BigDecimal.ZERO;

    BigDecimal usedByInstallments = installmentRepository.sumUnpaidAmountByCardId(cardId);
    LocalDate endOfMonth = LocalDate.now().with(lastDayOfMonth());
    BigDecimal usedBySubscriptions = subscriptionRepository.sumActiveAmountByCardId(cardId, endOfMonth);

    BigDecimal usedAmount = usedByInstallments.add(usedBySubscriptions);
    BigDecimal availableLimit = creditLimit.subtract(usedAmount);

    return new AvailableLimitResponse(creditLimit, usedAmount, availableLimit);
  }
}
