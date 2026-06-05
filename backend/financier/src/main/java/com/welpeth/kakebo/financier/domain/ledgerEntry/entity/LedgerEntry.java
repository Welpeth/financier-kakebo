package com.welpeth.kakebo.financier.domain.ledgerEntry.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.journal.entity.Journal;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class LedgerEntry extends BaseEntity {

  @Column(name = "name")
  private String name;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]")
  @Column(name = "final_date")
  private LocalDateTime finalDate;

  //Foreign Keys
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_journal")
  private Journal journal;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_transaction")
  private Transaction transaction;
}
