package com.muhammad_anas.backend_mart.store;

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
import com.muhammad_anas.backend_mart.store.dto.StoreCreateRequest;
import com.muhammad_anas.backend_mart.store.dto.StoreResponse;
import com.muhammad_anas.backend_mart.store.dto.StoreUpdateRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("${app.api.base-path}/stores")
public class StoreController {
    
    private final StoreService storeService;

    @GetMapping
    public ApiResponse<PageResponse<StoreResponse>> list(
            @RequestParam(required = false) String provinceName,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "false") boolean whitelistedOnly,
            @RequestParam(defaultValue = "false") boolean includeInactive,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {

        return ApiResponse.ok(storeService.list(provinceName, branchId, name,
                whitelistedOnly, includeInactive, pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<StoreResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(storeService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StoreResponse>> create(
            @Valid @RequestBody StoreCreateRequest req) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Store created", storeService.create(req)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<StoreResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StoreUpdateRequest req) {

        return ApiResponse.ok("Store updated", storeService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        storeService.softDelete(id);
        return ApiResponse.ok("Store deleted", null);
    }
}
