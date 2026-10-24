package com.muhammad_anas.backend_mart.branch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BranchCreateRequest(
        
    @NotNull(message = "provinceId is required")
    Long provinceId,

    @NotBlank(message = "code is required")
    @Size(max = 20, message = "code must be at most 20 characters")
    String code,

    @NotBlank(message = "name is required")
    @Size(max = 150, message = "name must be at most 150 characters")
    String name,

    String address,

    @Size(max = 20, message = "phone must be at most 20 characters")
    String phone,
    
    Boolean active
) {
    
}
