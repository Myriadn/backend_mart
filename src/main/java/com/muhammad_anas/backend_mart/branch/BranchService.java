package com.muhammad_anas.backend_mart.branch;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muhammad_anas.backend_mart.audit.AuditLogService;
import com.muhammad_anas.backend_mart.branch.dto.BranchCreateRequest;
import com.muhammad_anas.backend_mart.branch.dto.BranchResponse;
import com.muhammad_anas.backend_mart.branch.dto.BranchUpdateRequest;
import com.muhammad_anas.backend_mart.common.api.PageResponse;
import com.muhammad_anas.backend_mart.common.exception.BusinessException;
import com.muhammad_anas.backend_mart.common.exception.ResourceNotFoundException;
import com.muhammad_anas.backend_mart.common.specification.SpecificationBuilder;
import com.muhammad_anas.backend_mart.province.Province;
import com.muhammad_anas.backend_mart.province.ProvinceRepository;
import com.muhammad_anas.backend_mart.security.SecurityUtils;
import com.muhammad_anas.backend_mart.store.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional(readOnly = true)
public class BranchService {
    
    private final BranchRepository branchRepository;
    private final ProvinceRepository provinceRepository;
    private final AuditLogService auditLogService;
    private final StoreRepository storeRepository;

    public PageResponse<BranchResponse> list(Long provinceId, String name,
                                             boolean includeInactive, Pageable pageable) {
        Specification<Branch> spec = SpecificationBuilder.notDeleted();

        if (!includeInactive) {
            spec = spec.and(SpecificationBuilder.isActive());
        }
        if (provinceId != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("province").get("id"), provinceId));
        }
        if (name != null && !name.isBlank()) {
            spec = spec.and(SpecificationBuilder.likeIgnoreCase("name", name));
        }

        Page<Branch> page = branchRepository.findAll(spec, pageable);
        return PageResponse.of(page, BranchMapper::toResponse);
    }

    public BranchResponse getById(Long id) {
        Branch branch = branchRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", id));
        return BranchMapper.toResponse(branch);
    }

    @Transactional
    public BranchResponse create(BranchCreateRequest req) {
        if (branchRepository.existsByCodeAndDeletedFalse(req.code())) {
            throw new BusinessException("Branch code already exists: " + req.code());
        }

        Province province = provinceRepository.findByIdAndDeletedFalse(req.provinceId())
                .orElseThrow(() -> new ResourceNotFoundException("Province", req.provinceId()));

        Branch branch = BranchMapper.toEntity(req, province);
        branch = branchRepository.save(branch);

        auditLogService.logCreate("Branch", branch.getId(),
                BranchMapper.toAuditSnapshot(branch));

        return BranchMapper.toResponse(branch);
    }

    @Transactional
    public BranchResponse update(Long id, BranchUpdateRequest req) {
        Branch branch = branchRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", id));

        Map<String, Object> oldSnapshot = BranchMapper.toAuditSnapshot(branch);
        BranchMapper.updateEntity(branch, req);
        branch = branchRepository.save(branch);

        auditLogService.logUpdate("Branch", branch.getId(),
                oldSnapshot, BranchMapper.toAuditSnapshot(branch));

        return BranchMapper.toResponse(branch);
    }

    @Transactional
    public void softDelete(Long id) {
        Branch branch = branchRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", id));
        
        if (storeRepository.existsByBranchIdAndDeletedFalse(id)) {
            throw new BusinessException(
                    "Cannot delete branch with active stores. Deactivate or delete stores first.");
        }

        Map<String, Object> oldSnapshot = BranchMapper.toAuditSnapshot(branch);

        branch.setDeleted(true);
        branch.setDeletedAt(Instant.now());
        branch.setDeletedBy(SecurityUtils.currentUsername());
        branchRepository.save(branch);

        auditLogService.logDelete("Branch", branch.getId(), oldSnapshot);
    }
}
