package com.welpeth.kakebo.financier.domain.user.entity;

import com.welpeth.kakebo.financier.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Holder extends BaseEntity {

  private String password;

  private String email;
}
