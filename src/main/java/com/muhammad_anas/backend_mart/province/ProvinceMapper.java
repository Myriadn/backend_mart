package com.muhammad_anas.backend_mart.province;

import java.util.HashMap;
import java.util.Map;

import com.muhammad_anas.backend_mart.province.dto.ProvinceCreateRequest;
import com.muhammad_anas.backend_mart.province.dto.ProvinceResponse;
import com.muhammad_anas.backend_mart.province.dto.ProvinceUpdateRequest;

public final class ProvinceMapper {
    
    private ProvinceMapper() {}

    public static Province toEntity(ProvinceCreateRequest req) {
        Province p = new Province();
        p.setCode(req.code());
        p.setName(req.name());
        p.setActive(req.active() == null || req.active());
        return p;
    }

    public static void updateEntity(Province p, ProvinceUpdateRequest req) {
        if (req.name() != null && !req.name().isBlank()) {
            p.setName(req.name());
        }
        if (req.active() != null) {
            p.setActive(req.active());
        }
    }

    public static ProvinceResponse toResponse(Province p) {
        return new ProvinceResponse(
                p.getId(),
                p.getCode(),
                p.getName(),
                p.isActive(),
                p.getCreatedBy(),
                p.getCreatedAt(),
                p.getUpdatedBy(),
                p.getUpdatedAt()
        );
    }

    /**
     * Flat snapshot for audit. Never pass the JPA entity directly —
     * lazy associations may trigger LazyInitializationException on serialization.
     */
    public static Map<String, Object> toAuditSnapshot(Province p) {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("id", p.getId());
        snapshot.put("code", p.getCode());
        snapshot.put("name", p.getName());
        snapshot.put("active", p.isActive());
        return snapshot;
    }
}
