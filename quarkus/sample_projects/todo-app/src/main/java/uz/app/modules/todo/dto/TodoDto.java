package uz.app.modules.todo.dto;

import uz.app.modules.todo.entity.Todo;

public class TodoDto {

    public record response(
            Long id,
            String title,
            String description,
            Boolean completed
            ) {

    }

    public record create(
            String title,
            String description,
            Long userId
            ) {

        public Todo toEntity() {
            final Todo todo = new Todo();

            todo.title = this.title;
            todo.description = this.description;
            todo.completed = false;

            return todo;
        }
    }

    public record Criteria(
            Long userId,
            Boolean completed,
            java.time.LocalDateTime from,
            java.time.LocalDateTime to
            ) {

    }
}
