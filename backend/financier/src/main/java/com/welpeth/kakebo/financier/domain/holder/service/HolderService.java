package com.welpeth.kakebo.financier.domain.holder.service;

import com.welpeth.kakebo.financier.config.JwtService;
import com.welpeth.kakebo.financier.domain.holder.dto.LoginRequest;
import com.welpeth.kakebo.financier.domain.holder.dto.RegisterRequest;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.holder.dto.LoginResponse;
import com.welpeth.kakebo.financier.domain.holder.repository.HolderRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class HolderService {

  private final HolderRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public LoginResponse login(LoginRequest request) {
    Holder user = userRepository.findUserByEmail(request.email())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
    }

    return new LoginResponse(jwtService.generateToken(user));
  }

  public void register(RegisterRequest request) {
    if (userRepository.findUserByEmail(request.email()).isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
    }

    Holder holder = new Holder();
    holder.setId(UUID.randomUUID());
    holder.setName(request.name());
    holder.setEmail(request.email());
    holder.setPassword(passwordEncoder.encode(request.password()));

    userRepository.save(holder);
  }
}
