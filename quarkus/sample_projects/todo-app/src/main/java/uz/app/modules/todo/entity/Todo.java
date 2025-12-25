package uz.app.modules.todo.entity;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uz.app.modules.user.entity.User;

@Entity
@Table(name = "todos")
public class Todo extends PanacheEntity {

    @NotBlank
    @Column(nullable = false, length = 255)
    public String title;

    @Column(length = 1000)
    public String description;

    @NotNull
    @Column(nullable = false)
    public Boolean completed = false;

    @NotNull
    @Column(nullable = false)
    public Integer priority = 3; // 1=HIGH, 2=MEDIUM, 3=LOW

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User user;

    @Column(name = "due_at")
    public LocalDateTime dueAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    /* ===== Lifecycle ===== */
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
