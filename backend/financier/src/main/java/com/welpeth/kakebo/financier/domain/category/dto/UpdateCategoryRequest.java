package com.welpeth.kakebo.financier.domain.category.dto;

import java.util.UUID;

public record UpdateCategoryRequest(
    UUID id,
    String name
) {
}
