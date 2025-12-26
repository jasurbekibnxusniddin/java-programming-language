package uz.app.modules.user.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "email")
        }
)
public class User extends PanacheEntity {

    @NotBlank
    @Column(nullable = false, length = 100)
    public String name;

    @Email
    @NotBlank
    @Column(nullable = false, length = 150, unique = true)
    public String email;

    @NotBlank
    @Column(nullable = false)
    public String password;

    @NotBlank
    @Column(nullable = false)
    public String role;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @SuppressWarnings("unused")
    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @SuppressWarnings("unused")
    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
