package com.muhammad_anas.backend_mart.common.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@MappedSuperclass 
@EntityListeners (AuditingEntityListener.class)
public abstract class BaseAuditEntity {

    @CreatedDate 
    @Column (nullable = false, updatable = false)
    private Instant createdAt;

    @CreatedBy 
    @Column(updatable = false, length = 50)
    private String createdBy;

    @LastModifiedDate 
    private Instant updatedAt;

    @LastModifiedBy 
    @Column(length = 50)
    private String updatedBy;

    @Column(nullable = false)
    private boolean deleted = false;

    private Instant deletedAt;

    @Column(length = 50)
    private String deletedBy;

    @Version 
    private Long version;
}
