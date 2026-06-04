package com.welpeth.kakebo.financier.domain.userAddress.entity;

import com.welpeth.kakebo.financier.domain.address.entity.Address;
import com.welpeth.kakebo.financier.domain.user.entity.Holder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class HolderAddress {

  @EmbeddedId
  private HolderAddressId id = new HolderAddressId();

  @ManyToOne
  @MapsId("idHolder")
  @JoinColumn(name = "id_holder")
  private Holder user;

  @ManyToOne
  @MapsId("idAddress")
  @JoinColumn(name = "id_address")
  private Address address;

}