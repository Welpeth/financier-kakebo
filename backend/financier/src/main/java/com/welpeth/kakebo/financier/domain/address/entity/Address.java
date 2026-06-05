package com.welpeth.kakebo.financier.domain.address.entity;

import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.holderAddress.entity.HolderAddress;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Address extends BaseEntity {

  // Foreign Keys
  @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
  private List<HolderAddress> holders;
}