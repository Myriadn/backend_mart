package com.muhammad_anas.backend_mart.common.specification;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationBuilder {
    
    private SpecificationBuilder() {}

    public static <T> Specification<T> notDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }

    public static <T> Specification<T> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static <T> Specification<T> equalTo(String field, Object value) {
        return (root, query, cb) -> value == null
                ? cb.conjunction()
                : cb.equal(root.get(field), value);
    }

    public static <T> Specification<T> likeIgnoreCase(String field, String value) {
        return (root, query, cb) -> (value == null || value.isBlank())
                ? cb.conjunction()
                : cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }
}
