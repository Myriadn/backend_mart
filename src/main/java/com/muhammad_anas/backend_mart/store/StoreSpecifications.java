package com.muhammad_anas.backend_mart.store;

import org.springframework.data.jpa.domain.Specification;

import com.muhammad_anas.backend_mart.branch.Branch;
import com.muhammad_anas.backend_mart.province.Province;

import jakarta.persistence.criteria.Join;

public final class StoreSpecifications {
    
    private StoreSpecifications() {}

    public static Specification<Store> inProvince(String provinceName) {
        return (root, query, cb) -> {
            if (provinceName == null || provinceName.isBlank()) {
                return cb.conjunction();
            }
            Join<Store, Branch> branch = root.join("branch");
            Join<Branch, Province> province = branch.join("province");
            return cb.like(cb.lower(province.get("name")),
                    "%" + provinceName.toLowerCase().trim() + "%");
        };
    }

    public static Specification<Store> whitelistedOnly() {
        return (root, query, cb) -> cb.isTrue(root.get("whitelisted"));
    }

    public static Specification<Store> inBranch(Long branchId) {
        return (root, query, cb) -> branchId == null
                ? cb.conjunction()
                : cb.equal(root.get("branch").get("id"), branchId);
    }
}
