package com.oocl.todo_list.controller;

import com.oocl.todo_list.model.TodoItem;
import com.oocl.todo_list.repository.TodoItemRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TodoItemControllerTest {

    @Autowired
    private MockMvc client;

    @Autowired
    private TodoItemRepository todoItemRepository;

    @Autowired
    private JacksonTester<List<TodoItem>> todosJacksonTester;

    @BeforeEach
    void setUp() {
        todoItemRepository.deleteAll();
        todoItemRepository.flush();
        todoItemRepository.save(new TodoItem(1, "todo 1", false));
        todoItemRepository.save(new TodoItem(2, "todo 2", false));
        todoItemRepository.save(new TodoItem(3, "todo 3", false));
    }

    @Test
    void should_return_todos_when_get_all_given_todo_exist() throws Exception {
        //given
        final List<TodoItem> givenTodos = todoItemRepository.findAll();

        //when
        //then
        final String jsonResponse = client.perform(MockMvcRequestBuilders.get("/todo-items"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        final List<TodoItem> todoItemsResult = todosJacksonTester.parseObject(jsonResponse);
        assertThat(todoItemsResult)
                .usingRecursiveFieldByFieldElementComparator(
                        RecursiveComparisonConfiguration.builder()
                                .withComparedFields( "id", "text", "done")
                                .build()
                )
                .isEqualTo(givenTodos);
    }

    @Test
    void should_save_todo_success() throws Exception {
        // Given
        todoItemRepository.deleteAll();
        String givenText = "new todo";
        Boolean givenDone = false;
        String givenTodo = String.format(
                "{\"text\": \"%s\", \"done\": \"%s\"}",
                givenText,
                givenDone
        );

        // When
        // Then
        client.perform(MockMvcRequestBuilders.post("/todo-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTodo)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(givenText))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(givenDone));
        List<TodoItem> todoItems = todoItemRepository.findAll();
        assertThat(todoItems).hasSize(1);
        AssertionsForClassTypes.assertThat(todoItems.get(0).getId()).isNotNull();
        AssertionsForClassTypes.assertThat(todoItems.get(0).getText()).isEqualTo(givenText);
        AssertionsForClassTypes.assertThat(todoItems.get(0).getDone()).isEqualTo(givenDone);
    }

    @Test
    void should_update_todo_success() throws Exception {
        // Given
        TodoItem existTodo = todoItemRepository.findAll().get(0);
        Integer givenId = existTodo.getId();
        String givenText = "updated text";
        Boolean givenDone = true;
        String givenEmployee = String.format(
                "{\"text\": \"%s\", \"done\": \"%s\"}",
                givenText,
                givenDone
        );

        // When
        // Then
        client.perform(MockMvcRequestBuilders.put("/todo-items/" + givenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenEmployee)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(givenText))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(givenDone));
        List<TodoItem> todoItems = todoItemRepository.findAll();
        assertThat(todoItems).hasSize(3);
        AssertionsForClassTypes.assertThat(todoItems.get(0).getId()).isNotNull();
        AssertionsForClassTypes.assertThat(todoItems.get(0).getText()).isEqualTo(givenText);
        AssertionsForClassTypes.assertThat(todoItems.get(0).getDone()).isEqualTo(givenDone);
    }

    @Test
    void should_remove_todo_success() throws Exception {
        TodoItem existTodoId = todoItemRepository.findAll().get(0);
        int givenId = existTodoId.getId();
        // When
        // Then
        client.perform(MockMvcRequestBuilders.delete("/todo-items/" + givenId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        List<TodoItem> todoItems = todoItemRepository.findAll();
        assertThat(todoItems).hasSize(2);
    }
}