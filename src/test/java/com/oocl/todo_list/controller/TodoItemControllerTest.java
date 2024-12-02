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
}