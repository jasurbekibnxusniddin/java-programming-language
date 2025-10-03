package com.example.todo.module.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.todo.module.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    // Find todos by user ID
    List<Todo> findByUserId(Long userId);
    
    // Find todos by completion status
    List<Todo> findByCompleted(boolean completed);
    
    // Find todo with user using JOIN FETCH
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user WHERE t.id = :id")
    Optional<Todo> findByIdWithUser(@Param("id") Long id);
    
    // Find todos by user using JOIN FETCH
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user WHERE t.user.id = :userId")
    List<Todo> findByUserIdWithUser(@Param("userId") Long userId);
    
    // Search todos with title and completion status (JPQL)
    @Query("""
        SELECT t FROM Todo t 
        WHERE t.title LIKE %:keyword% AND t.completed = :completed
    """)
    List<Todo> searchTodos(@Param("keyword") String keyword, @Param("completed") boolean completed);
    
    // Find todos with user details using JOIN
    @Query("""
        SELECT t FROM Todo t 
        JOIN t.user u 
        WHERE u.username LIKE %:username%
    """)
    List<Todo> findByUsernameLike(@Param("username") String username);
    
    // Count todos by user using JOIN
    @Query("""
        SELECT COUNT(t) FROM Todo t 
        JOIN t.user u 
        WHERE u.id = :userId
    """)
    long countTodosByUserId(@Param("userId") Long userId);
    
    // Find completed todos with user details
    @Query("""
        SELECT t FROM Todo t 
        JOIN FETCH t.user u 
        WHERE t.completed = true
    """)
    List<Todo> findCompletedTodosWithUser();
    
    // Find pending todos with user details
    @Query("""
        SELECT t FROM Todo t 
        JOIN FETCH t.user u 
        WHERE t.completed = false
    """)
    List<Todo> findPendingTodosWithUser();
    
    // Native query: Get todo statistics by user
    @Query(value = """
        SELECT u.username, 
               COUNT(t.id) as total_todos,
               COUNT(CASE WHEN t.completed = true THEN 1 END) as completed_todos,
               COUNT(CASE WHEN t.completed = false THEN 1 END) as pending_todos
        FROM users u 
        LEFT JOIN todos t ON u.id = t.user_id 
        WHERE u.id = :userId
        GROUP BY u.id, u.username
    """, nativeQuery = true)
    Object[] getTodoStatisticsByUser(@Param("userId") Long userId);
    
    // Native query: Search todos with user details
    @Query(value = """
        SELECT t.id, t.title, t.completed, u.id as user_id, u.username 
        FROM todos t 
        INNER JOIN users u ON t.user_id = u.id 
        WHERE t.title ILIKE CONCAT('%', :keyword, '%')
    """, nativeQuery = true)
    List<Object[]> searchTodosWithUserDetails(@Param("keyword") String keyword);
}