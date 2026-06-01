package com.welpeth.kakebo.financier.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "user_address")
public class UserAddress {

  @EmbeddedId
  private UserAddressId id = new UserAddressId();

  @ManyToOne
  @MapsId("idUser")
  @JoinColumn(name = "id_user")
  private User user;

  @ManyToOne
  @MapsId("idAddress")
  @JoinColumn(name = "id_address")
  private Address address;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "created_by")
  private LocalDateTime createdBy;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}