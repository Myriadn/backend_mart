package com.muhammad_anas.backend_mart.province;

import java.time.Instant;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muhammad_anas.backend_mart.audit.AuditLogService;
import com.muhammad_anas.backend_mart.branch.BranchRepository;
import com.muhammad_anas.backend_mart.common.api.PageResponse;
import com.muhammad_anas.backend_mart.common.exception.BusinessException;
import com.muhammad_anas.backend_mart.common.exception.ResourceNotFoundException;
import com.muhammad_anas.backend_mart.common.specification.SpecificationBuilder;
import com.muhammad_anas.backend_mart.province.dto.ProvinceCreateRequest;
import com.muhammad_anas.backend_mart.province.dto.ProvinceResponse;
import com.muhammad_anas.backend_mart.province.dto.ProvinceUpdateRequest;
import com.muhammad_anas.backend_mart.security.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
@Transactional(readOnly = true)
public class ProvinceService {
    
    private final ProvinceRepository provinceRepository;
    private final AuditLogService auditLogService;
    private final BranchRepository branchRepository;

    public PageResponse<ProvinceResponse> list(String name, boolean includeInactive, Pageable pageable) {
        Specification<Province> spec = SpecificationBuilder.notDeleted();

        if (!includeInactive) {
            spec = spec.and(SpecificationBuilder.isActive());
        }
        if (name != null && !name.isBlank()) {
            spec = spec.and(SpecificationBuilder.likeIgnoreCase("name", name));
        }

        Page<Province> page = provinceRepository.findAll(spec, pageable);
        return PageResponse.of(page, ProvinceMapper::toResponse);
    }

    public ProvinceResponse getById(Long id) {
        Province province = provinceRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Province", id));
        return ProvinceMapper.toResponse(province);
    }

    @Transactional
    public ProvinceResponse create(ProvinceCreateRequest req) {
        if (provinceRepository.existsByCodeAndDeletedFalse(req.code())) {
            throw new BusinessException("Province code already exists: " + req.code());
        }

        Province province = ProvinceMapper.toEntity(req);
        province = provinceRepository.save(province);

        auditLogService.logCreate("Province", province.getId(),
                ProvinceMapper.toAuditSnapshot(province));

        return ProvinceMapper.toResponse(province);
    }

    @Transactional
    public ProvinceResponse update(Long id, ProvinceUpdateRequest req) {
        Province province = provinceRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Province", id));

        Map<String, Object> oldSnapshot = ProvinceMapper.toAuditSnapshot(province);
        ProvinceMapper.updateEntity(province, req);
        province = provinceRepository.save(province);

        auditLogService.logUpdate("Province", province.getId(),
                oldSnapshot, ProvinceMapper.toAuditSnapshot(province));

        return ProvinceMapper.toResponse(province);
    }

    @Transactional
    public void softDelete(Long id) {
        Province province = provinceRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Province", id));
        
        if (branchRepository.existsByProvinceIdAndDeletedFalse(id)) {
            throw new BusinessException(
                "Cannot delete province with active branches. Deactivate or delete branches first.");
        }

        Map<String, Object> oldSnapshot = ProvinceMapper.toAuditSnapshot(province);

        province.setDeleted(true);
        province.setDeletedAt(Instant.now());
        province.setDeletedBy(SecurityUtils.currentUsername());
        provinceRepository.save(province);

        auditLogService.logDelete("Province", province.getId(), oldSnapshot);
    }
}
