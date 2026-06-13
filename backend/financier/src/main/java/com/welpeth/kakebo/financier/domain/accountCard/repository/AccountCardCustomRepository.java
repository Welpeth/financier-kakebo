package com.welpeth.kakebo.financier.domain.accountCard.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.accountCard.dto.UpdateAccountCardRequest;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import jakarta.transaction.Transactional;

public interface AccountCardCustomRepository extends BaseCustomRepository<AccountCard> {

  @Transactional
  void update(UpdateAccountCardRequest request);
}
