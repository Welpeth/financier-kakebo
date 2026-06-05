package com.welpeth.kakebo.financier.domain.transaction.entity;

import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.account.entity.Account;import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Transaction extends BaseEntity {

  // Foreign Keys
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_account")
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_account_card")
  private AccountCard accountCard;

}
