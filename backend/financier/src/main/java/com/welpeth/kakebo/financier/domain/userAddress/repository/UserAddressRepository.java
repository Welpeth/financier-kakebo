package com.welpeth.kakebo.financier.domain.userAddress.repository;

import com.welpeth.kakebo.financier.domain.userAddress.entity.HolderAddress;
import com.welpeth.kakebo.financier.domain.userAddress.entity.HolderAddressId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<HolderAddress, HolderAddressId> {
}