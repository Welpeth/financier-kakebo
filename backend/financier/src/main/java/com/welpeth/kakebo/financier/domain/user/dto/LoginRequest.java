package com.welpeth.kakebo.financier.domain.user.dto;

public record LoginRequest(
    String email,
    String password
) {
}
