package com.welpeth.kakebo.financier.domain.holder.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.holder.dto.LoginRequest;
import com.welpeth.kakebo.financier.domain.holder.dto.LoginResponse;
import com.welpeth.kakebo.financier.domain.holder.dto.RegisterRequest;
import com.welpeth.kakebo.financier.domain.holder.service.HolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HolderController {

  private final HolderService holderService;

  @PostMapping(ApiPath.AUTH_LOGIN)
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    return ResponseEntity.status(HttpStatus.OK).body(holderService.login(request));
  }

  @PostMapping(ApiPath.AUTH_REGISTER)
  public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
    holderService.register(request);
    return ResponseEntity.noContent().build();
  }
}
