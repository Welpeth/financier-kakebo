package com.welpeth.kakebo.financier.domain.holderAddress.service;

import com.welpeth.kakebo.financier.domain.holderAddress.dto.CreateHolderAddressRequest;
import com.welpeth.kakebo.financier.domain.holderAddress.entity.HolderAddress;
import com.welpeth.kakebo.financier.domain.holderAddress.entity.HolderAddressId;
import com.welpeth.kakebo.financier.domain.holderAddress.repository.UserAddressRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAddressService {

  private final UserAddressRepository repository;

  public HolderAddress create(CreateHolderAddressRequest request) {
    HolderAddress holderAddress = new HolderAddress();
    holderAddress.setHolder(request.holder());
    holderAddress.setAddress(request.address());
    repository.save(holderAddress);
    return holderAddress;
  }

  public HolderAddress get(UUID idHolder, UUID idAddress) {
    return repository.getReferenceById(new HolderAddressId(idHolder, idAddress));
  }

  public void delete(UUID idHolder, UUID idAddress) {
    repository.deleteById(new HolderAddressId(idHolder, idAddress));
  }

  public List<HolderAddress> getList() {
    return repository.findAll();
  }
}
