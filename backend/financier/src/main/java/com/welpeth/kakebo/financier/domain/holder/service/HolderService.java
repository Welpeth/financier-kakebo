package com.welpeth.kakebo.financier.domain.holder.service;

import com.welpeth.kakebo.financier.config.JwtService;
import com.welpeth.kakebo.financier.domain.holder.dto.LoginRequest;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.holder.dto.LoginResponse;
import com.welpeth.kakebo.financier.domain.holder.repository.HolderRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HolderService {

  private final HolderRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public LoginResponse login(LoginRequest request) {

    Holder user = userRepository.findUserByEmail(request.email())
        .orElseThrow(() -> new RuntimeException("User not Found"));

    boolean validPassword = passwordEncoder.matches(request.password(), user.getPassword());

    if (!validPassword) {
      throw new RuntimeException();
    }

    String token = jwtService.generateToken(user);

    return new LoginResponse(token);
  }
}
