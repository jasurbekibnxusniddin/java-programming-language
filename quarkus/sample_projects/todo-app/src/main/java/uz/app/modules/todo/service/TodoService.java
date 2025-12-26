package uz.app.modules.todo.service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import uz.app.modules.todo.dto.TodoDto;
import uz.app.modules.todo.entity.Todo;
import uz.app.modules.todo.repository.TodoRepository;
import uz.app.modules.user.entity.User;
import uz.app.modules.user.repository.UserRepository;

@ApplicationScoped
public class TodoService {

    @Inject
    TodoRepository todoRepository;

    @Inject
    UserRepository userRepository;

    @Transactional
    public TodoDto.response create(TodoDto.create in) {
        final Todo todo = in.toEntity();
        
        if (in.userId() != null) {
            User user = userRepository.findById(in.userId());
            if (user == null) {
                throw new NotFoundException("User not found with id: " + in.userId());
            }
            todo.user = user;
        } else {
            // For now, if no user ID provided, we fail as per entity constraint.
            // In a real auth scenario, this would come from the security context.
            throw new IllegalArgumentException("User ID is required to create a Todo");
        }

        todoRepository.persist(todo);
        return toResponse(todo);
    }

    public TodoDto.response get(Long id) {
        return todoRepository.findByIdOptional(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Todo not found with id: " + id));
    }

    public List<TodoDto.response> list() {
        return todoRepository.findAll().list().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TodoDto.response update(Long id, TodoDto.create in) {
        Todo todo = todoRepository.findById(id);
        if (todo == null) {
            throw new NotFoundException("Todo not found with id: " + id);
        }
        
        todo.title = in.title();
        todo.description = in.description();
        
        // We generally don't update the user of a todo, but if needed logic can go here.
        
        return toResponse(todo);
    }

    @Transactional
    public void delete(Long id) {
        boolean deleted = todoRepository.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("Todo not found with id: " + id);
        }
    }

    private TodoDto.response toResponse(Todo todo) {
        return new TodoDto.response(
                todo.id,
                todo.title,
                todo.description,
                todo.completed
        );
    }
}
