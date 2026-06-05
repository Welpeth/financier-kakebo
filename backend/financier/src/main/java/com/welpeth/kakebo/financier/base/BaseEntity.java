package com.welpeth.kakebo.financier.base;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  public BaseEntity(UUID id) {
    this.id = id;
  }

  @Id
  @Nonnull
  @Column(name = "id", updatable = false, unique = true, nullable = false)
  private UUID id;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]")
  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @CreatedBy
  @Column(name = "created_by")
  private String createdBy;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]")
  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || !(this.getClass().equals(obj.getClass()))) {
      return false;
    }
    final BaseEntity that = (BaseEntity) obj;
    return this.id.equals(that.getId());
  }

  @Override
  public int hashCode() {
    return id == null ? 0 : id.hashCode();
  }
}