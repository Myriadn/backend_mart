package com.muhammad_anas.backend_mart.branch.dto;

import java.time.Instant;

public record BranchResponse(
    Long id,
    String code,
    String name,
    String address,
    String phone,
    boolean active,
    Long provinceId,
    String provinceName,
    String createdBy,
    Instant createdAt,
    String updatedBy,
    Instant updatedAt
) {
    
}
