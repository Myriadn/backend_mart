package com.muhammad_anas.backend_mart.store.dto;

import java.time.Instant;

public record StoreResponse(
    Long id,
    String code,
    String name,
    String address,
    boolean whitelisted,
    boolean active,
    Long branchId,
    String branchName,
    Long provinceId,
    String provinceName,
    String createdBy,
    Instant createdAt,
    String updatedBy,
    Instant updatedAt
) {
    
}
