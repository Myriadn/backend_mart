package com.muhammad_anas.backend_mart.store;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StoreRepository extends JpaRepository<Store, Long>, 
                                            JpaSpecificationExecutor<Store> {
    Optional<Store> findByIdAndDeletedFalse(Long id);

    boolean existsByCodeAndDeletedFalse(String code);

    boolean existsByBranchIdAndDeletedFalse(Long branchId);
}
