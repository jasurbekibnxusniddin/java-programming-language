package uz.app.modules.todo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import uz.app.modules.todo.entity.Todo;

public class TodoRepository implements PanacheRepository<Todo> {
}
