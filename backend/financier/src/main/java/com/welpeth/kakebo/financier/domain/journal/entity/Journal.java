package com.welpeth.kakebo.financier.domain.journal.entity;

import com.welpeth.kakebo.financier.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Journal extends BaseEntity {

  @Id
  private UUID id;

}
