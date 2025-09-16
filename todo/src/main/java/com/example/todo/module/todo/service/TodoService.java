package com.example.todo.module.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.todo.module.todo.dto.TodoDto;
import com.example.todo.module.todo.entity.Todo;
import com.example.todo.module.todo.repository.TodoRepository;
import com.example.todo.module.user.repository.UserRepository;

@Service
public class TodoService {
    
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public Optional<TodoDto.Response> create(TodoDto.Create createDto) {
        return userRepository.findById(createDto.getUserId())
                .map(user -> {
                    Todo todo = new Todo(createDto.getTitle(), createDto.isCompleted(), user);
                    Todo saved = todoRepository.save(todo);
                    return new TodoDto.Response(
                        saved.getId(),
                        saved.getTitle(),
                        saved.isCompleted(),
                        user.getId(),
                        user.getUsername()
                    );
                });
    }
    
    public List<TodoDto.SimpleResponse> findAll() {
        return todoRepository.findAll().stream()
                .map(todo -> new TodoDto.SimpleResponse(
                    todo.getId(), 
                    todo.getTitle(), 
                    todo.isCompleted()
                ))
                .toList();
    }
    
    public Optional<TodoDto.Response> findById(Long id) {
        return todoRepository.findByIdWithUser(id)
                .map(todo -> new TodoDto.Response(
                    todo.getId(),
                    todo.getTitle(),
                    todo.isCompleted(),
                    todo.getUser() != null ? todo.getUser().getId() : null,
                    todo.getUser() != null ? todo.getUser().getUsername() : null
                ));
    }
    
    public List<TodoDto.Response> findByUserId(Long userId) {
        return todoRepository.findByUserIdWithUser(userId).stream()
                .map(todo -> new TodoDto.Response(
                    todo.getId(),
                    todo.getTitle(),
                    todo.isCompleted(),
                    todo.getUser().getId(),
                    todo.getUser().getUsername()
                ))
                .toList();
    }
    
    public List<TodoDto.SimpleResponse> findByCompleted(boolean completed) {
        return todoRepository.findByCompleted(completed).stream()
                .map(todo -> new TodoDto.SimpleResponse(
                    todo.getId(), 
                    todo.getTitle(), 
                    todo.isCompleted()
                ))
                .toList();
    }
    
    public Optional<TodoDto.Response> update(Long id, TodoDto.Update updateDto) {
        return todoRepository.findByIdWithUser(id)
                .map(todo -> {
                    if (updateDto.getTitle() != null) {
                        todo.setTitle(updateDto.getTitle());
                    }
                    if (updateDto.getCompleted() != null) {
                        todo.setCompleted(updateDto.getCompleted());
                    }
                    if (updateDto.getUserId() != null) {
                        userRepository.findById(updateDto.getUserId())
                                .ifPresent(todo::setUser);
                    }
                    Todo updated = todoRepository.save(todo);
                    return new TodoDto.Response(
                        updated.getId(),
                        updated.getTitle(),
                        updated.isCompleted(),
                        updated.getUser() != null ? updated.getUser().getId() : null,
                        updated.getUser() != null ? updated.getUser().getUsername() : null
                    );
                });
    }
    
    public boolean deleteById(Long id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Custom query methods using joins
    public List<TodoDto.SimpleResponse> searchTodos(String keyword, boolean completed) {
        return todoRepository.searchTodos(keyword, completed).stream()
                .map(todo -> new TodoDto.SimpleResponse(
                    todo.getId(), 
                    todo.getTitle(), 
                    todo.isCompleted()
                ))
                .toList();
    }
    
    public List<TodoDto.Response> findByUsernameLike(String username) {
        return todoRepository.findByUsernameLike(username).stream()
                .map(todo -> new TodoDto.Response(
                    todo.getId(),
                    todo.getTitle(),
                    todo.isCompleted(),
                    todo.getUser().getId(),
                    todo.getUser().getUsername()
                ))
                .toList();
    }
    
    public long countTodosByUserId(Long userId) {
        return todoRepository.countTodosByUserId(userId);
    }
    
    public List<TodoDto.Response> findCompletedTodosWithUser() {
        return todoRepository.findCompletedTodosWithUser().stream()
                .map(todo -> new TodoDto.Response(
                    todo.getId(),
                    todo.getTitle(),
                    todo.isCompleted(),
                    todo.getUser().getId(),
                    todo.getUser().getUsername()
                ))
                .toList();
    }
    
    public List<TodoDto.Response> findPendingTodosWithUser() {
        return todoRepository.findPendingTodosWithUser().stream()
                .map(todo -> new TodoDto.Response(
                    todo.getId(),
                    todo.getTitle(),
                    todo.isCompleted(),
                    todo.getUser().getId(),
                    todo.getUser().getUsername()
                ))
                .toList();
    }
    
    public Object[] getTodoStatisticsByUser(Long userId) {
        return todoRepository.getTodoStatisticsByUser(userId);
    }
    
    public List<Object[]> searchTodosWithUserDetails(String keyword) {
        return todoRepository.searchTodosWithUserDetails(keyword);
    }
}