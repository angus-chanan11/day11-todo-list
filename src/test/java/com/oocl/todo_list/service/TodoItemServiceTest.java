package com.oocl.todo_list.service;

import com.oocl.todo_list.model.TodoItem;
import com.oocl.todo_list.repository.TodoItemRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TodoItemServiceTest {

    @Test
    void should_return_the_given_todos_when_getAllTodoItems() {
        //given
        TodoItemRepository todoItemRepository = mock(TodoItemRepository.class);
        when(todoItemRepository.findAll()).thenReturn(List.of(new TodoItem(1, "todo 1", false)));
        TodoItemService todoItemService = new TodoItemService(todoItemRepository);

        //when
        List<TodoItem> allTodos = todoItemService.getAll();

        //then
        assertEquals(1, allTodos.size());
        assertEquals("todo 1", allTodos.get(0).getText());
    }
}