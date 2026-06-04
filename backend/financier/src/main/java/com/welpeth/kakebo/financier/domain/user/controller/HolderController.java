package com.welpeth.kakebo.financier.domain.user.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.user.dto.LoginRequest;
import com.welpeth.kakebo.financier.domain.user.dto.LoginResponse;
import com.welpeth.kakebo.financier.domain.user.service.HolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HolderController {

  private final HolderService holderService;

  @PostMapping(ApiPath.AUTH_LOGIN)
  public LoginResponse login(@RequestBody LoginRequest request){
    return holderService.login(request);
  }
}
