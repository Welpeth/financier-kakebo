package com.welpeth.kakebo.financier.domain.journal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.category.entity.Category;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.LedgerEntry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Journal extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "total_value")
  private BigDecimal totalValue;

  @JsonIgnore
  @OneToMany(mappedBy = "journal", fetch = FetchType.LAZY)
  private List<LedgerEntry> ledgerEntries;

}
