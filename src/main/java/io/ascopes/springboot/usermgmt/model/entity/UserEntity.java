package io.ascopes.springboot.usermgmt.model.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Database entity description for a user.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    indexes = @Index(columnList = "userName", unique = true),
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "userName"),
        @UniqueConstraint(columnNames = "email")
    }
)
@Validated
public class UserEntity {
    @Column(nullable = false, updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;

    @Column(nullable = false)
    @NotNull
    @Size(min = 3, max = 25)
    private String userName;

    @Column(nullable = false)
    @NotNull
    private String email;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    @Getter(onMethod_ = @Nullable)
    @Setter(AccessLevel.NONE)
    private long createdAt;

    @Column(nullable = false, updatable = false)
    @CreatedBy
    @Getter(onMethod_ = @Nullable)
    @Setter(AccessLevel.NONE)
    private String createdBy;

    @Column(nullable = false)
    @LastModifiedDate
    @Getter(onMethod_ = @Nullable)
    @Setter(AccessLevel.NONE)
    private long lastModifiedAt;

    @Column(nullable = false)
    @LastModifiedBy
    @Getter(onMethod_ = @Nullable)
    @Setter(AccessLevel.NONE)
    private String lastModifiedBy;
}
