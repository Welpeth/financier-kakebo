package com.welpeth.kakebo.financier.domain.holderAddress.repository;

import com.welpeth.kakebo.financier.domain.holderAddress.entity.HolderAddress;
import com.welpeth.kakebo.financier.domain.holderAddress.entity.HolderAddressId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<HolderAddress, HolderAddressId> {
}