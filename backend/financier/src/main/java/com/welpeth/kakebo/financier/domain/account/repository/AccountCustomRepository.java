package com.welpeth.kakebo.financier.domain.account.repository;

import com.welpeth.kakebo.financier.base.BaseCustomRepository;
import com.welpeth.kakebo.financier.domain.account.dto.UpdateAccountRequest;import com.welpeth.kakebo.financier.domain.account.entity.Account;import jakarta.transaction.Transactional;

public interface AccountCustomRepository extends BaseCustomRepository<Account> {

@Transactional void update(UpdateAccountRequest request);}
