package com.example.todo.module.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.todo.module.todo.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query(
      value = """
        SELECT * FROM todo 
        WHERE title ILIKE CONCAT('%', :keyword, '%') AND completed = :completed
      """,
      nativeQuery = true
    )
    List<Todo> searchTodos(@Param("keyword") String keyword, @Param("completed") boolean completed);
}