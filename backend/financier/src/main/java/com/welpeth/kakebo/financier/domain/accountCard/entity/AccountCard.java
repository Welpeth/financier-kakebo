package com.welpeth.kakebo.financier.domain.accountCard.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.accountCard.type.CardType;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AccountCard extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "is_active")
  private Boolean isActive;

  @Enumerated(EnumType.STRING)
  @Column(name = "card_type")
  private CardType type;

  @Column(name = "credit_limit")
  private BigDecimal creditLimit;

  @Column(name = "expiration_month", nullable = false)
  private Integer expirationMonth;

  @Column(name = "expiration_year", nullable = false)
  private Integer expirationYear;

  // Foreign Keys
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_account")
  private Account account;

  @JsonIgnore
  @OneToMany(mappedBy = "accountCard", fetch = FetchType.LAZY)
  private List<Transaction> transactions;

}
