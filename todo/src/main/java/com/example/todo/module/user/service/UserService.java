package com.example.todo.module.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.todo.module.user.dto.UserDto;
import com.example.todo.module.user.entity.User;
import com.example.todo.module.user.repository.UserRepository;


@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto.Response create(UserDto.SignUp in) {
        User user = new User(in.getUsername(), in.getPassword());
        User saved = userRepository.save(user);
        return new UserDto.Response(saved.getId(), saved.getUsername());
    }
    
    public List<UserDto.Response> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto.Response(user.getId(), user.getUsername()))
                .toList();
    }
    
    public Optional<UserDto.Response> findById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDto.Response(user.getId(), user.getUsername()));
    }
    
    public Optional<UserDto.Response> findByIdWithTodos(Long id) {
        return userRepository.findByIdWithTodos(id)
                .map(user -> new UserDto.Response(user.getId(), user.getUsername()));
    }
    
    public Optional<UserDto.Response> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserDto.Response(user.getId(), user.getUsername()));
    }
    
    public Optional<UserDto.Response> update(Long id, UserDto.Update updateDto) {
        return userRepository.findById(id)
                .map(user -> {
                    if (updateDto.getUsername() != null) {
                        user.setUsername(updateDto.getUsername());
                    }
                    if (updateDto.getPassword() != null) {
                        user.setPassword(updateDto.getPassword());
                    }
                    User updated = userRepository.save(user);
                    return new UserDto.Response(updated.getId(), updated.getUsername());
                });
    }
    
    public boolean deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Custom query methods using joins
    public List<UserDto.Response> findUsersWithMinTodoCount(long minTodoCount) {
        return userRepository.findUsersWithMinTodoCount(minTodoCount).stream()
                .map(user -> new UserDto.Response(user.getId(), user.getUsername()))
                .toList();
    }
    
    public List<UserDto.Response> findUsersWithCompletedTodos() {
        return userRepository.findUsersWithCompletedTodos().stream()
                .map(user -> new UserDto.Response(user.getId(), user.getUsername()))
                .toList();
    }
    
    public List<UserDto.Response> findUsersByTodoTitle(String title) {
        return userRepository.findUsersByTodoTitle(title).stream()
                .map(user -> new UserDto.Response(user.getId(), user.getUsername()))
                .toList();
    }
    
    public Object[] getUserStatistics(Long userId) {
        return userRepository.getUserStatistics(userId);
    }
}
