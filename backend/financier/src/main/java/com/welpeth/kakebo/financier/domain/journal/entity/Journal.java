package com.welpeth.kakebo.financier.domain.journal.entity;

import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.category.entity.Category;
import com.welpeth.kakebo.financier.domain.ledgerEntry.entity.LedgerEntry;import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
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

  // Foreign Keys
  @OneToMany(mappedBy = "journal")
  private List<Category> categories;

  @OneToMany(mappedBy = "journal")
  private List<LedgerEntry> ledgerEntries;

}
