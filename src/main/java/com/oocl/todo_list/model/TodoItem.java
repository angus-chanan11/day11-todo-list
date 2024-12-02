package com.oocl.todo_list.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TodoItem {
    @Id
    private Integer id;
    private String text;
    private Boolean done;

    public TodoItem() {}

    public TodoItem(Integer id, String text, Boolean done) {
        this.id = id;
        this.text = text;
        this.done = done;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public void update(TodoItem todoItem) {
        this.text = todoItem.getText() == null ? this.text : todoItem.getText();
        this.done = todoItem.getDone() == null ? this.done : todoItem.getDone();
    }
}
