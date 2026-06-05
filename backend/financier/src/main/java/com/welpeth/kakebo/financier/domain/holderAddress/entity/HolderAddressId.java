package com.welpeth.kakebo.financier.domain.holderAddress.entity;

import jakarta.persistence.Column;import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class HolderAddressId implements Serializable {

  @Column(name = "id_holder")
  private UUID idHolder;

  @Column(name = "id_address")
  private UUID idAddress;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HolderAddressId that = (HolderAddressId) o;
    return Objects.equals(idHolder, that.idHolder) && Objects.equals(idAddress, that.idAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idHolder, idAddress);
  }
}