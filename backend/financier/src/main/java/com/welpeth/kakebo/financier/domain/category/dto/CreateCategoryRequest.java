package com.welpeth.kakebo.financier.domain.category.dto;

import com.welpeth.kakebo.financier.domain.journal.entity.Journal;

public record CreateCategoryRequest(
    String name,
    Journal journal
) {
}
