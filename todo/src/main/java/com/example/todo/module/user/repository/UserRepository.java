package com.example.todo.module.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.todo.module.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by username
    Optional<User> findByUsername(String username);
    
    // Find user with their todos using JOIN FETCH
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.todos WHERE u.id = :id")
    Optional<User> findByIdWithTodos(@Param("id") Long id);
    
    // Find users with todo count using JOIN
    @Query("""
        SELECT u FROM User u 
        LEFT JOIN u.todos t 
        GROUP BY u.id, u.username, u.password 
        HAVING COUNT(t) >= :minTodoCount
    """)
    List<User> findUsersWithMinTodoCount(@Param("minTodoCount") long minTodoCount);
    
    // Find users who have completed todos
    @Query("""
        SELECT DISTINCT u FROM User u 
        INNER JOIN u.todos t 
        WHERE t.completed = true
    """)
    List<User> findUsersWithCompletedTodos();
    
    // Find users by todo title using JOIN
    @Query("""
        SELECT DISTINCT u FROM User u 
        INNER JOIN u.todos t 
        WHERE t.title LIKE %:title%
    """)
    List<User> findUsersByTodoTitle(@Param("title") String title);
    
    // Native query: Get user statistics with todo counts
    @Query(value = """
        SELECT u.id, u.username, u.password,
               COUNT(t.id) as total_todos,
               COUNT(CASE WHEN t.completed = true THEN 1 END) as completed_todos
        FROM users u 
        LEFT JOIN todos t ON u.id = t.user_id 
        WHERE u.id = :userId
        GROUP BY u.id, u.username, u.password
    """, nativeQuery = true)
    Object[] getUserStatistics(@Param("userId") Long userId);
}
