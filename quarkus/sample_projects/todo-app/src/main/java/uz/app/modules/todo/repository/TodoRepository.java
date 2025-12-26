package uz.app.modules.todo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import uz.app.modules.todo.entity.Todo;

@ApplicationScoped
public class TodoRepository implements PanacheRepository<Todo> {
}
