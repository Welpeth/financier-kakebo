package com.welpeth.kakebo.financier.domain.account.entity;

import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Account extends BaseEntity {

  //Foreign Keys
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_holder")
  private Holder holder;

  @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
  private List<AccountCard> accountCards;

  @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
  private List<Transaction> transactions;
}
