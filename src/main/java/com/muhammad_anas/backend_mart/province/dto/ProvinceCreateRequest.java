package com.muhammad_anas.backend_mart.province.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProvinceCreateRequest(

        @NotBlank(message = "code is required")
        @Size(max = 10, message = "code must be at most 10 characters")
        String code,

        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        Boolean active
) {}
