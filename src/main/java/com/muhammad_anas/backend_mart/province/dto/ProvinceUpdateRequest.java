package com.muhammad_anas.backend_mart.province.dto;

import jakarta.validation.constraints.Size;

public record ProvinceUpdateRequest(

    @Size(max = 100, message = "name must be at most 100 characters")
    String name,
    
    Boolean active
) {
    
}
