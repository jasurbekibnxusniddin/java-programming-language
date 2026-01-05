package uz.app.modules.todo.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

    @Inject
    EntityManager entityManager;

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

    public List<TodoDto.response> list(TodoDto.Criteria criteria) {

        CriteriaBuilder filter = entityManager.getCriteriaBuilder();
        CriteriaQuery<Todo> query = filter.createQuery(Todo.class);
        Root<Todo> root = query.from(Todo.class);

        List<Predicate> condition = new ArrayList<>();

        if (criteria.userId() != null) {
            condition.add(filter.equal(root.get("user").get("id"), criteria.userId()));
        }

        if (criteria.completed() != null) {
            condition.add(filter.equal(root.get("completed"), criteria.completed()));
        }

        if (criteria.from() != null && criteria.to() != null) {
            condition.add(filter.between(root.get("createdAt"), criteria.from(), criteria.to()));
        } else if (criteria.from() != null) {
            condition.add(filter.greaterThanOrEqualTo(root.get("createdAt"), criteria.from()));
        } else if (criteria.to() != null) {
            condition.add(filter.lessThanOrEqualTo(root.get("createdAt"), criteria.to()));
        }

        if (!condition.isEmpty()) {
            query.where(condition.toArray(Predicate[]::new));
        }

        return entityManager.createQuery(query).getResultList().stream()
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
