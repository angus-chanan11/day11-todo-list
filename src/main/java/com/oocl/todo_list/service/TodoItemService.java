package com.oocl.todo_list.service;

import com.oocl.todo_list.exception.TodoItemNotFoundException;
import com.oocl.todo_list.model.TodoItem;
import com.oocl.todo_list.repository.TodoItemRepository;
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

    public TodoItem findById(Integer id) {
        return todoItemRepository.findById(id).orElseThrow(TodoItemNotFoundException::new);
    }

    public TodoItem create(TodoItem todoItem) {
        return todoItemRepository.save(todoItem);
    }

    public TodoItem update(Integer id, TodoItem todoItem) {
        TodoItem existingTodoItem = todoItemRepository.findById(id).orElseThrow(TodoItemNotFoundException::new);

        existingTodoItem.update(todoItem);
        return todoItemRepository.save(existingTodoItem);
    }

    public void delete(Integer id) {
        todoItemRepository.deleteById(id);
    }
}
