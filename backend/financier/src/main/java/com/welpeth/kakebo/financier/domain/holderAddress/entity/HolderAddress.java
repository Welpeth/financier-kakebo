package com.welpeth.kakebo.financier.domain.holderAddress.entity;

import com.welpeth.kakebo.financier.domain.address.entity.Address;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class HolderAddress {

  @EmbeddedId
  private HolderAddressId id = new HolderAddressId();

  @MapsId("idHolder")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_holder", nullable = false)
  private Holder holder;

  @MapsId("idAddress")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_address", nullable = false)
  private Address address;

}