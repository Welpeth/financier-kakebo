package com.welpeth.kakebo.financier.domain.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;
import com.welpeth.kakebo.financier.domain.category.entity.Category;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.LedgerEntry;
import com.welpeth.kakebo.financier.domain.transaction.type.TransactionType;
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
public class Transaction extends BaseEntity {

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "fee")
  private BigDecimal fee;

  @Column(name = "installment")
  private Integer installment;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private TransactionType type;

  @Column(name = "description")
  private String description;

  @Column(name = "is_subscription")
  private boolean subscription;

  // Foreign Keys
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_account")
  private Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_account_card")
  private AccountCard accountCard;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_category")
  private Category category;

  @JsonIgnore
  @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY)
  private List<LedgerEntry> ledgerEntries;

}
