package com.welpeth.kakebo.financier.domain.holderAddress.dto;

import com.welpeth.kakebo.financier.domain.address.entity.Address;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;

public record CreateHolderAddressRequest(Holder holder, Address address) {}
