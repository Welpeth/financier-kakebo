package com.welpeth.kakebo.financier.domain.installment.entity;

import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.installmentPurchase.entity.InstallmentPurchase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "installment")
public class Installment extends BaseEntity {

  @Column(name = "installment_number", nullable = false)
  private Integer installmentNumber;

  @Column(name = "amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "due_date", nullable = false)
  private LocalDate dueDate;

  @Column(name = "paid", nullable = false)
  private Boolean paid = false;

  @Column(name = "paid_at")
  private LocalDate paidAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "installment_purchase_id", nullable = false)
  private InstallmentPurchase installmentPurchase;
}
