package com.muhammad_anas.backend_mart.branch;

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

import com.muhammad_anas.backend_mart.branch.dto.BranchCreateRequest;
import com.muhammad_anas.backend_mart.branch.dto.BranchResponse;
import com.muhammad_anas.backend_mart.branch.dto.BranchUpdateRequest;
import com.muhammad_anas.backend_mart.common.api.ApiResponse;
import com.muhammad_anas.backend_mart.common.api.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
@RequestMapping("${app.api.base-path}/branches")
public class BranchController {
    
    private final BranchService branchService;

    @GetMapping
    public ApiResponse<PageResponse<BranchResponse>> list(
            @RequestParam(required = false) Long provinceId,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "false") boolean includeInactive,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {

        return ApiResponse.ok(branchService.list(provinceId, name, includeInactive, pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<BranchResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(branchService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BranchResponse>> create(
            @Valid @RequestBody BranchCreateRequest req) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Branch created", branchService.create(req)));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<BranchResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BranchUpdateRequest req) {

        return ApiResponse.ok("Branch updated", branchService.update(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        branchService.softDelete(id);
        return ApiResponse.ok("Branch deleted", null);
    }
}
