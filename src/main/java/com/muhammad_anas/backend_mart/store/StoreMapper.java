package com.muhammad_anas.backend_mart.store;

import java.util.HashMap;
import java.util.Map;

import com.muhammad_anas.backend_mart.branch.Branch;
import com.muhammad_anas.backend_mart.store.dto.StoreCreateRequest;
import com.muhammad_anas.backend_mart.store.dto.StoreResponse;
import com.muhammad_anas.backend_mart.store.dto.StoreUpdateRequest;

public final class StoreMapper {
    
    private StoreMapper() {}

    public static Store toEntity(StoreCreateRequest req, Branch branch) {
        Store s = new Store();
        s.setBranch(branch);
        s.setCode(req.code());
        s.setName(req.name());
        s.setAddress(req.address());
        s.setWhitelisted(req.whitelisted() != null && req.whitelisted());
        s.setActive(req.active() == null || req.active());
        return s;
    }

    public static void updateEntity(Store s, StoreUpdateRequest req) {
        if (req.name() != null && !req.name().isBlank()) {
            s.setName(req.name());
        }
        if (req.address() != null) {
            s.setAddress(req.address());
        }
        if (req.whitelisted() != null) {
            s.setWhitelisted(req.whitelisted());
        }
        if (req.active() != null) {
            s.setActive(req.active());
        }
    }

    public static StoreResponse toResponse(Store s) {
        Branch b = s.getBranch();
        return new StoreResponse(
                s.getId(),
                s.getCode(),
                s.getName(),
                s.getAddress(),
                s.isWhitelisted(),
                s.isActive(),
                b.getId(),
                b.getName(),
                b.getProvince().getId(),
                b.getProvince().getName(),
                s.getCreatedBy(),
                s.getCreatedAt(),
                s.getUpdatedBy(),
                s.getUpdatedAt()
        );
    }

    public static Map<String, Object> toAuditSnapshot(Store s) {
        Map<String, Object> snap = new HashMap<>();
        snap.put("id", s.getId());
        snap.put("code", s.getCode());
        snap.put("name", s.getName());
        snap.put("address", s.getAddress());
        snap.put("whitelisted", s.isWhitelisted());
        snap.put("active", s.isActive());
        snap.put("branchId", s.getBranch() != null ? s.getBranch().getId() : null);
        return snap;
    }
}
