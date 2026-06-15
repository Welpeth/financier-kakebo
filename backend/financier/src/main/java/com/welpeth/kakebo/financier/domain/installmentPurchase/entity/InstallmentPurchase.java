package com.welpeth.kakebo.financier.domain.installmentPurchase.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.installment.entity.Installment;
import com.welpeth.kakebo.financier.domain.transaction.entity.Transaction;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "installment_purchase")
public class InstallmentPurchase extends BaseEntity {

  @Column(name = "total_amount", nullable = false)
  private BigDecimal totalAmount;

  @Column(name = "interest_rate")
  private BigDecimal interestRate;

  @Column(name = "total_amount_with_interest")
  private BigDecimal totalAmountWithInterest;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "transaction_id", nullable = false)
  private Transaction transaction;

  @JsonIgnore
  @OneToMany(mappedBy = "installmentPurchase", fetch = FetchType.LAZY)
  private List<Installment> installments;
}
