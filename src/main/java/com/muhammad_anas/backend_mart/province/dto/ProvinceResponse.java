package com.muhammad_anas.backend_mart.province.dto;

import java.time.Instant;

public record ProvinceResponse(
    Long id,
    String code,
    String name,
    boolean active,
    String createdBy,
    Instant createdAt,
    String updatedBy,
    Instant updatedAt
) {
    
}
