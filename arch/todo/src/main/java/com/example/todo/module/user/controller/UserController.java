package com.example.todo.module.user.controller;

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

import com.example.todo.module.user.service.UserService;
import com.example.todo.module.user.dto.UserDto;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public UserDto.Response create(@RequestBody UserDto.SignUp in) {
        return userService.create(in);
    }
    
    @GetMapping
    public List<UserDto.Response> getAllUsers() {
        return userService.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto.Response> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/with-todos")
    public ResponseEntity<UserDto.Response> getUserByIdWithTodos(@PathVariable Long id) {
        return userService.findByIdWithTodos(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<UserDto.Response> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserDto.Response> updateUser(@PathVariable Long id, @RequestBody UserDto.Update updateDto) {
        return userService.update(id, updateDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    // Custom queries with joins
    @GetMapping("/with-min-todos")
    public List<UserDto.Response> getUsersWithMinTodoCount(@RequestParam(defaultValue = "1") long minTodoCount) {
        return userService.findUsersWithMinTodoCount(minTodoCount);
    }
    
    @GetMapping("/with-completed-todos")
    public List<UserDto.Response> getUsersWithCompletedTodos() {
        return userService.findUsersWithCompletedTodos();
    }
    
    @GetMapping("/by-todo-title")
    public List<UserDto.Response> getUsersByTodoTitle(@RequestParam String title) {
        return userService.findUsersByTodoTitle(title);
    }
    
    @GetMapping("/{id}/statistics")
    public ResponseEntity<Object[]> getUserStatistics(@PathVariable Long id) {
        Object[] stats = userService.getUserStatistics(id);
        if (stats != null) {
            return ResponseEntity.ok(stats);
        }
        return ResponseEntity.notFound().build();
    }
}
