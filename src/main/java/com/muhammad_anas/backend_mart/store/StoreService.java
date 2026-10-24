package com.muhammad_anas.backend_mart.store;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muhammad_anas.backend_mart.audit.AuditLogService;
import com.muhammad_anas.backend_mart.branch.Branch;
import com.muhammad_anas.backend_mart.branch.BranchRepository;
import com.muhammad_anas.backend_mart.common.api.PageResponse;
import com.muhammad_anas.backend_mart.common.exception.BusinessException;
import com.muhammad_anas.backend_mart.common.exception.ResourceNotFoundException;
import com.muhammad_anas.backend_mart.common.specification.SpecificationBuilder;
import com.muhammad_anas.backend_mart.security.SecurityUtils;
import com.muhammad_anas.backend_mart.store.dto.StoreCreateRequest;
import com.muhammad_anas.backend_mart.store.dto.StoreResponse;
import com.muhammad_anas.backend_mart.store.dto.StoreUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional(readOnly = true)
public class StoreService {
    
    private final StoreRepository storeRepository;
    private final BranchRepository branchRepository;
    private final AuditLogService auditLogService;

    public PageResponse<StoreResponse> list(String provinceName, Long branchId, String name,
                                            boolean whitelistedOnly, boolean includeInactive,
                                            Pageable pageable) {
        Specification<Store> spec = SpecificationBuilder.notDeleted();

        if (!includeInactive) {
            spec = spec.and(SpecificationBuilder.isActive());
        }
        spec = spec.and(StoreSpecifications.inProvince(provinceName));
        spec = spec.and(StoreSpecifications.inBranch(branchId));

        if (name != null && !name.isBlank()) {
            spec = spec.and(SpecificationBuilder.likeIgnoreCase("name", name));
        }
        if (whitelistedOnly) {
            spec = spec.and(StoreSpecifications.whitelistedOnly());
        }

        Page<Store> page = storeRepository.findAll(spec, pageable);
        return PageResponse.of(page, StoreMapper::toResponse);
    }

    public StoreResponse getById(Long id) {
        Store store = storeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", id));
        return StoreMapper.toResponse(store);
    }

    @Transactional
    public StoreResponse create(StoreCreateRequest req) {
        if (storeRepository.existsByCodeAndDeletedFalse(req.code())) {
            throw new BusinessException("Store code already exists: " + req.code());
        }

        Branch branch = branchRepository.findByIdAndDeletedFalse(req.branchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch", req.branchId()));

        Store store = StoreMapper.toEntity(req, branch);
        store = storeRepository.save(store);

        auditLogService.logCreate("Store", store.getId(),
                StoreMapper.toAuditSnapshot(store));

        return StoreMapper.toResponse(store);
    }

    @Transactional
    public StoreResponse update(Long id, StoreUpdateRequest req) {
        Store store = storeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", id));

        Map<String, Object> oldSnapshot = StoreMapper.toAuditSnapshot(store);
        StoreMapper.updateEntity(store, req);
        store = storeRepository.save(store);

        auditLogService.logUpdate("Store", store.getId(),
                oldSnapshot, StoreMapper.toAuditSnapshot(store));

        return StoreMapper.toResponse(store);
    }

    @Transactional
    public void softDelete(Long id) {
        Store store = storeRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", id));

        Map<String, Object> oldSnapshot = StoreMapper.toAuditSnapshot(store);

        store.setDeleted(true);
        store.setDeletedAt(Instant.now());
        store.setDeletedBy(SecurityUtils.currentUsername());
        storeRepository.save(store);

        auditLogService.logDelete("Store", store.getId(), oldSnapshot);
    }
}
