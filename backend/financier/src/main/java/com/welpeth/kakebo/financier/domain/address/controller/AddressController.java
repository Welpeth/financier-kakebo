package com.welpeth.kakebo.financier.domain.address.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.address.dto.CreateAddressRequest;
import com.welpeth.kakebo.financier.domain.address.entity.Address;
import com.welpeth.kakebo.financier.domain.address.service.AddressService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.ADDRESS)
public class AddressController {

  private final AddressService addressService;

  @GetMapping("/{id}")
  public ResponseEntity<Address> getById(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(addressService.get(id));
  }

  @GetMapping("/list")
  public ResponseEntity<List<Address>> getList() {
    return ResponseEntity.status(HttpStatus.OK).body(addressService.getList());
  }

  @PostMapping
  public ResponseEntity<Address> create(@RequestBody CreateAddressRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    addressService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
