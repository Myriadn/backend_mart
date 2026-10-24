package com.muhammad_anas.backend_mart.branch.dto;

import jakarta.validation.constraints.Size;

public record BranchUpdateRequest(
    
    @Size(max = 150, message = "name must be at most 150 characters")
    String name,

    String address,

    @Size(max = 20, message = "phone must be at most 20 characters")
    String phone,

    Boolean active
) {
    
}
