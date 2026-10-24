package com.muhammad_anas.backend_mart.province;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProvinceRepository extends JpaRepository<Province, Long>, 
                                                JpaSpecificationExecutor<Province> {
    
    Optional<Province> findByIdAndDeletedFalse(Long id);

    boolean existsByCodeAndDeletedFalse(String code);
    
}
