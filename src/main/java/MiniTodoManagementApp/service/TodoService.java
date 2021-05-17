package MiniTodoManagementApp.service;

import MiniTodoManagementApp.model.Todo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TodoService {

    List<Todo> getTodoByuser(String user);

    Optional<Todo> getTodoById(long id);

    void updateTodo(Todo todo);

    void addTodo(String name, String desc, Date targetDate, boolean isDone);

    void deleteTod(long id);

    void saveTodo(Todo todo);

}
