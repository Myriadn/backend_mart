package com.muhammad_anas.backend_mart.province;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muhammad_anas.backend_mart.common.api.ApiResponse;
import com.muhammad_anas.backend_mart.common.api.PageResponse;
import com.muhammad_anas.backend_mart.province.dto.ProvinceCreateRequest;
import com.muhammad_anas.backend_mart.province.dto.ProvinceResponse;
import com.muhammad_anas.backend_mart.province.dto.ProvinceUpdateRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("${app.api.base-path}/provinces")
@RequiredArgsConstructor 
public class ProvinceController {
    
    private final ProvinceService provinceService;

    @GetMapping
    public ApiResponse<PageResponse<ProvinceResponse>> list(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "false") boolean includeInactive,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {

        return ApiResponse.ok(provinceService.list(name, includeInactive, pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProvinceResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(provinceService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProvinceResponse>> create(
            @Valid @RequestBody ProvinceCreateRequest req) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Province created", provinceService.create(req)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ProvinceResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProvinceUpdateRequest req) {

        return ApiResponse.ok("Province updated", provinceService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        provinceService.softDelete(id);
        return ApiResponse.ok("Province deleted", null);
    }
}
