package com.muhammad_anas.backend_mart.branch;

import java.util.HashMap;
import java.util.Map;

import com.muhammad_anas.backend_mart.branch.dto.BranchCreateRequest;
import com.muhammad_anas.backend_mart.branch.dto.BranchResponse;
import com.muhammad_anas.backend_mart.branch.dto.BranchUpdateRequest;
import com.muhammad_anas.backend_mart.province.Province;

public final class BranchMapper {
    
    private BranchMapper() {}

    public static Branch toEntity(BranchCreateRequest req, Province province) {
        Branch b = new Branch();
        b.setProvince(province);
        b.setCode(req.code());
        b.setName(req.name());
        b.setAddress(req.address());
        b.setPhone(req.phone());
        b.setActive(req.active() == null || req.active());
        return b;
    }

    public static void updateEntity(Branch b, BranchUpdateRequest req) {
        if (req.name() != null && !req.name().isBlank()) {
            b.setName(req.name());
        }
        if (req.address() != null) {
            b.setAddress(req.address());
        }
        if (req.phone() != null) {
            b.setPhone(req.phone());
        }
        if (req.active() != null) {
            b.setActive(req.active());
        }
    }

    public static BranchResponse toResponse(Branch b) {
        // b.getProvince() triggers lazy load 
        // must be inside an active transaction
        return new BranchResponse(
                b.getId(),
                b.getCode(),
                b.getName(),
                b.getAddress(),
                b.getPhone(),
                b.isActive(),
                b.getProvince().getId(),
                b.getProvince().getName(),
                b.getCreatedBy(),
                b.getCreatedAt(),
                b.getUpdatedBy(),
                b.getUpdatedAt()
        );
    }

    public static Map<String, Object> toAuditSnapshot(Branch b) {
        Map<String, Object> s = new HashMap<>();
        s.put("id", b.getId());
        s.put("code", b.getCode());
        s.put("name", b.getName());
        s.put("address", b.getAddress());
        s.put("phone", b.getPhone());
        s.put("active", b.isActive());
        s.put("provinceId", b.getProvince() != null ? b.getProvince().getId() : null);
        return s;
    }
}
