package com.welpeth.kakebo.financier.domain.holder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.welpeth.kakebo.financier.base.BaseEntity;
import com.welpeth.kakebo.financier.domain.account.entity.Account;
import com.welpeth.kakebo.financier.domain.holderAddress.entity.HolderAddress;
import jakarta.persistence.Column;
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

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @JsonIgnore
  @Column(name = "password", nullable = false)
  private String password;

  // Foreign Keys
  @JsonIgnore
  @OneToMany(mappedBy = "holder", fetch = FetchType.LAZY)
  private List<HolderAddress> addresses;

  @JsonIgnore
  @OneToMany(mappedBy = "holder", fetch = FetchType.LAZY)
  private List<Account> accounts;
}
