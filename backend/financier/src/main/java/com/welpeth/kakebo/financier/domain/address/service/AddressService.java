package com.welpeth.kakebo.financier.domain.address.service;

import com.welpeth.kakebo.financier.domain.address.dto.CreateAddressRequest;
import com.welpeth.kakebo.financier.domain.address.entity.Address;
import com.welpeth.kakebo.financier.domain.address.repository.AddressRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository repository;

  public Address create(CreateAddressRequest request) {
    Address address = new Address();
    address.setId(UUID.randomUUID());
    repository.save(address);
    return address;
  }

  public Address get(UUID id) {
    return repository.getReferenceById(id);
  }

  public void delete(UUID id) {
    repository.deleteById(id);
  }

  public List<Address> getList() {
    return repository.findAll();
  }
}
