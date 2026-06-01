package com.welpeth.kakebo.financier.entity;

import jakarta.persistence.Embeddable;
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
public class UserAddressId implements Serializable {

  private UUID idUser;
  private UUID idAddress;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserAddressId that = (UserAddressId) o;
    return Objects.equals(idUser, that.idUser) && Objects.equals(idAddress, that.idAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUser, idAddress);
  }
}