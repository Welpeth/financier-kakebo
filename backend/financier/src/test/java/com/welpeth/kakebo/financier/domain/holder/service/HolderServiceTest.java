package com.welpeth.kakebo.financier.domain.holder.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.welpeth.kakebo.financier.config.JwtService;
import com.welpeth.kakebo.financier.domain.holder.dto.LoginRequest;
import com.welpeth.kakebo.financier.domain.holder.dto.LoginResponse;
import com.welpeth.kakebo.financier.domain.holder.dto.RegisterRequest;
import com.welpeth.kakebo.financier.domain.holder.entity.Holder;
import com.welpeth.kakebo.financier.domain.holder.repository.HolderRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class HolderServiceTest {

  @Mock
  private HolderRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private JwtService jwtService;

  @InjectMocks
  private HolderService service;

  @Test
  @DisplayName("login retorna o token quando credenciais são válidas")
  void loginReturnsTokenForValidCredentials() {
    // Arrange
    Holder user = new Holder();
    user.setEmail("henrique@example.com");
    user.setPassword("hashed");
    LoginRequest request = new LoginRequest("henrique@example.com", "segredo");
    when(userRepository.findUserByEmail("henrique@example.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("segredo", "hashed")).thenReturn(true);
    when(jwtService.generateToken(user)).thenReturn("jwt-token");

    // Act
    LoginResponse response = service.login(request);

    // Assert
    assertThat(response.token()).isEqualTo("jwt-token");
  }

  @Test
  @DisplayName("login lança 401 quando o usuário não existe")
  void loginThrowsWhenUserMissing() {
    // Arrange
    LoginRequest request = new LoginRequest("ausente@example.com", "segredo");
    when(userRepository.findUserByEmail("ausente@example.com")).thenReturn(Optional.empty());

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.login(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(ex.getReason()).isEqualTo("Credenciais inválidas");
  }

  @Test
  @DisplayName("login lança 401 quando a senha não confere")
  void loginThrowsWhenPasswordMismatch() {
    // Arrange
    Holder user = new Holder();
    user.setPassword("hashed");
    LoginRequest request = new LoginRequest("henrique@example.com", "errada");
    when(userRepository.findUserByEmail("henrique@example.com")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("errada", "hashed")).thenReturn(false);

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.login(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    verify(jwtService, never()).generateToken(any());
  }

  @Test
  @DisplayName("register lança 409 quando o e-mail já está cadastrado")
  void registerThrowsWhenEmailExists() {
    // Arrange
    RegisterRequest request = new RegisterRequest("Henrique", "henrique@example.com", "segredo");
    when(userRepository.findUserByEmail("henrique@example.com"))
        .thenReturn(Optional.of(new Holder()));

    // Act
    ResponseStatusException ex =
        assertThrows(ResponseStatusException.class, () -> service.register(request));

    // Assert
    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(ex.getReason()).isEqualTo("E-mail já cadastrado");
    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("register codifica a senha e persiste o novo holder")
  void registerEncodesPasswordAndSaves() {
    // Arrange
    RegisterRequest request = new RegisterRequest("Henrique", "henrique@example.com", "segredo");
    when(userRepository.findUserByEmail("henrique@example.com")).thenReturn(Optional.empty());
    when(passwordEncoder.encode("segredo")).thenReturn("hashed");

    // Act
    service.register(request);

    // Assert
    ArgumentCaptor<Holder> captor = ArgumentCaptor.forClass(Holder.class);
    verify(userRepository).save(captor.capture());
    Holder saved = captor.getValue();
    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getName()).isEqualTo("Henrique");
    assertThat(saved.getEmail()).isEqualTo("henrique@example.com");
    assertThat(saved.getPassword()).isEqualTo("hashed");
  }
}
