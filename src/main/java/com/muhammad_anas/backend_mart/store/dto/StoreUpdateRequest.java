package com.muhammad_anas.backend_mart.store.dto;

import jakarta.validation.constraints.Size;

public record StoreUpdateRequest(
    
    @Size(max = 150, message = "name must be at most 150 characters")
    String name,

    String address,

    Boolean whitelisted,

    Boolean active
) {
    
}
