package com.welpeth.kakebo.financier.domain.holder.controller;

import com.welpeth.kakebo.financier.config.ApiPath;
import com.welpeth.kakebo.financier.domain.holder.dto.LoginRequest;
import com.welpeth.kakebo.financier.domain.holder.dto.LoginResponse;
import com.welpeth.kakebo.financier.domain.holder.service.HolderService;
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
