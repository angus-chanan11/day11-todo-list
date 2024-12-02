package com.oocl.todoList.service;

import com.oocl.todoList.model.TodoItem;
import com.oocl.todoList.repository.TodoItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoItemService {
    private final TodoItemRepository todoItemRepository;

    public TodoItemService(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    public List<TodoItem> getAll() {
        return todoItemRepository.findAll();
    }
}
