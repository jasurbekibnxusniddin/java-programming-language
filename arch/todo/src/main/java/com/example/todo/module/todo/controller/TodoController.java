package com.example.todo.module.todo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.module.todo.dto.TodoDto;
import com.example.todo.module.todo.service.TodoService;

@RestController
@RequestMapping("/v1/todo")
public class TodoController {
    
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/create")
    public ResponseEntity<TodoDto.Response> create(@RequestBody TodoDto.Create createDto) {
        return todoService.create(createDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
    
    @GetMapping
    public List<TodoDto.SimpleResponse> getAllTodos() {
        return todoService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto.Response> getTodoById(@PathVariable Long id) {
        return todoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public List<TodoDto.Response> getTodosByUserId(@PathVariable Long userId) {
        return todoService.findByUserId(userId);
    }
    
    @GetMapping("/completed/{completed}")
    public List<TodoDto.SimpleResponse> getTodosByCompleted(@PathVariable boolean completed) {
        return todoService.findByCompleted(completed);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto.Response> updateTodo(@PathVariable Long id, @RequestBody TodoDto.Update updateDto) {
        return todoService.update(id, updateDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        if (todoService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // Custom queries with joins
    @GetMapping("/search")
    public List<TodoDto.SimpleResponse> searchTodos(
            @RequestParam String keyword, 
            @RequestParam boolean completed) {
        return todoService.searchTodos(keyword, completed);
    }
    
    @GetMapping("/by-username")
    public List<TodoDto.Response> getTodosByUsernameLike(@RequestParam String username) {
        return todoService.findByUsernameLike(username);
    }
    
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Long> countTodosByUserId(@PathVariable Long userId) {
        long count = todoService.countTodosByUserId(userId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/completed-with-user")
    public List<TodoDto.Response> getCompletedTodosWithUser() {
        return todoService.findCompletedTodosWithUser();
    }
    
    @GetMapping("/pending-with-user")
    public List<TodoDto.Response> getPendingTodosWithUser() {
        return todoService.findPendingTodosWithUser();
    }
    
    @GetMapping("/statistics/user/{userId}")
    public ResponseEntity<Object[]> getTodoStatisticsByUser(@PathVariable Long userId) {
        Object[] stats = todoService.getTodoStatisticsByUser(userId);
        if (stats != null) {
            return ResponseEntity.ok(stats);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search-with-user")
    public List<Object[]> searchTodosWithUserDetails(@RequestParam String keyword) {
        return todoService.searchTodosWithUserDetails(keyword);
    }
}