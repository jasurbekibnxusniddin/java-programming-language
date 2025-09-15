package com.example.todo.module.todo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.module.todo.dto.TodoDtos;
import com.example.todo.module.todo.entity.Todo;
import com.example.todo.module.todo.repository.TodoRepository;




@RestController
@RequestMapping("/api/todos")
public class TodoController {
    
    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }


    @PostMapping("/create")
    public TodoDtos.Response create(@RequestBody TodoDtos.Create dto) {
        Todo saved = todoRepository.save(new Todo(dto.getTitle(), false));
        return new TodoDtos.Response(saved.getId(), saved.getTitle(), saved.isCompleted());
    }

    @GetMapping("/search")
    public List<TodoDtos.Response> search() {
         return todoRepository.
                    findAll().
                    stream().
                    map(
                            t -> new TodoDtos.Response(
                                t.getId(), 
                                t.getTitle(), 
                                t.isCompleted()
                            )
                        ).
                    collect(Collectors.toList());
        
    }

    @GetMapping("/{id}")
    public TodoDtos.Response get(@PathVariable Long id) {
        Todo todo = todoRepository.
            findById(id).
            orElseThrow(() -> new RuntimeException("Todo not found: " + id));

        return new TodoDtos.Response(todo.getId(), todo.getTitle(), todo.isCompleted());
    }
    
    @PutMapping("/{id}")
    public TodoDtos.Response put(@PathVariable Long id, @RequestBody TodoDtos.Update in) {
        Todo updated = todoRepository.
            findById(id).
            map(
                exising -> {
                    exising.setTitle(in.getTitle()+"shunaqekan");
                    exising.setCompleted(in.isCompleted());
                    return todoRepository.save(exising);
                }).
            orElseThrow(() -> new RuntimeException("Todo not found: " + id));
            // orElseGet(() -> {
            //     throw new RuntimeException("Todo Not Found: " + id);
            // });
        
            return new TodoDtos.Response(updated.getId(), updated.getTitle(), updated.isCompleted());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        todoRepository.deleteById(id);
    }
}
