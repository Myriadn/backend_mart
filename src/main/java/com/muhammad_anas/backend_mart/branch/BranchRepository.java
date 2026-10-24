package com.muhammad_anas.backend_mart.branch;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BranchRepository extends JpaRepository<Branch, Long>,
                                            JpaSpecificationExecutor<Branch> {
    Optional<Branch> findByIdAndDeletedFalse(Long id);

    boolean existsByCodeAndDeletedFalse(String code);

    boolean existsByProvinceIdAndDeletedFalse(Long provinceId);
}
