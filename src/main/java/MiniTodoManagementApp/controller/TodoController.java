package MiniTodoManagementApp.controller;

import MiniTodoManagementApp.model.Todo;
import MiniTodoManagementApp.service.TodoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class TodoController {

    @Autowired
    private TodoServiceImpl todoService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    @GetMapping("/list-todos")
    public String showTodos(ModelMap modelMap) {
        String name = getLoggedinUserName();
        modelMap.addAttribute("todos", todoService.getTodoByuser(name));
        return "list-todos";
    }

    @GetMapping("/add-todo")
    public String showTodoPage(ModelMap modelMap) {
        modelMap.addAttribute("todo", new Todo());
        return "todo";
    }

    @GetMapping("/delete-todo")
    public String deleteTodo(@RequestParam long id) {
        todoService.deleteTod(id);
        return "redirect:/list-todos";
    }

    @GetMapping("/update-todo")
    public String showUpdateTodoPage(@RequestParam long id, ModelMap modelMap) {
        Todo todo = todoService.getTodoById(id).get();
        modelMap.addAttribute("todo", todo);
        return "todo";
    }

    @PostMapping("/update-todo")
    public String updateTodo(ModelMap modelMap, @Valid Todo todo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "todo";
        }

        todo.setUserName(getLoggedinUserName());
        todoService.updateTodo(todo);
        return "redirect:/list-todos";
    }

    @PostMapping("/add-todo")
    public String addTodo(ModelMap modelMap, Todo todo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "todo";
        }

        todo.setUserName(getLoggedinUserName());
        todoService.saveTodo(todo);
        return "redirect:/list-todos";
    }

    private String getLoggedinUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        return principal.toString();
    }

}
