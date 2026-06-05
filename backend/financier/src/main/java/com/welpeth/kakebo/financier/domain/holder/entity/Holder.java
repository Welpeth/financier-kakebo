package com.welpeth.kakebo.financier.domain.holder.entity;

import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.holderAddress.entity.HolderAddress;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Holder extends BaseEntity {

  private String password;

  private String email;

  // Foreign Keys
  @OneToMany(mappedBy = "holder", fetch = FetchType.LAZY)
  private List<HolderAddress> addresses;

  @OneToMany(mappedBy = "holder", fetch = FetchType.LAZY)
  private List<Account> accounts;
}
