package com.welpeth.kakebo.financier.domain.address.repository;

import com.welpeth.kakebo.financier.domain.address.entity.Address;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
