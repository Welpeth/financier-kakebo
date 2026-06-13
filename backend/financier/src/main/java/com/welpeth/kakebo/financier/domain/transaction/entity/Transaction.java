package com.welpeth.kakebo.financier.domain.transaction.entity;

import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.account.entity.Account;import com.welpeth.kakebo.financier.domain.accountCard.entity.AccountCard;import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
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

  @Column(name = "installment")
  private Integer installment;

  @Column(name = "fee")
  private BigDecimal fee;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private TransactionType type;

  @Column(name = "description")
  private String description;

  // Foreign Keys
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_account")
  private Account account;

  @Enumerated(EnumType.STRING)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_account_card")
  private AccountCard accountCard;

  @OneToMany(mappedBy = "transaction",fetch = FetchType.LAZY)
  private List<LedgerEntry> ledgerEntries;

}
