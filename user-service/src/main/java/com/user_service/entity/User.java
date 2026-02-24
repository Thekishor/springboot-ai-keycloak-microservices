package com.user_service.entity;

import com.user_service.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private String id;

    @Column(unique = true)
    private String keycloakId;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private Boolean isActive;

    @Column(name = "activate_token")
    private String activationToken;

    @Column(name = "token_expiry")
    private LocalDateTime activationTokenExpiry;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    public void setUserActivate() {
        if (this.isActive == null) {
            this.isActive = false;
        }
        if (this.role == null) {
            this.role = UserRole.USER;
        }
    }
}
