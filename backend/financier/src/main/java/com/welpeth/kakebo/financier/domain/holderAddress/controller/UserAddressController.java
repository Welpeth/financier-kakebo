package com.welpeth.kakebo.financier.domain.holderAddress.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.holderAddress.dto.CreateHolderAddressRequest;
import com.welpeth.kakebo.financier.domain.holderAddress.entity.HolderAddress;
import com.welpeth.kakebo.financier.domain.holderAddress.service.UserAddressService;
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
@RequestMapping(ApiPath.HOLDER_ADDRESS)
public class UserAddressController {

  private final UserAddressService userAddressService;

  @GetMapping("/{holderId}/{addressId}")
  public ResponseEntity<HolderAddress> getById(
      @PathVariable UUID holderId, @PathVariable UUID addressId) {
    return ResponseEntity.status(HttpStatus.OK).body(userAddressService.get(holderId, addressId));
  }

  @GetMapping("/list")
  public ResponseEntity<List<HolderAddress>> getList() {
    return ResponseEntity.status(HttpStatus.OK).body(userAddressService.getList());
  }

  @PostMapping
  public ResponseEntity<HolderAddress> create(@RequestBody CreateHolderAddressRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userAddressService.create(request));
  }

  @DeleteMapping("/{holderId}/{addressId}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID holderId, @PathVariable UUID addressId) {
    userAddressService.delete(holderId, addressId);
    return ResponseEntity.noContent().build();
  }
}
