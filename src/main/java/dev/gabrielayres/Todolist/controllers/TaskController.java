package dev.gabrielayres.Todolist.controllers;

import dev.gabrielayres.Todolist.tasks.TaskModel;
import dev.gabrielayres.Todolist.tasks.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskRepository repository;

    @PostMapping("/tasks")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {



        //taskModel.setUserId((UUID) request.getAttribute("userId"));

        System.out.println("================================");
        System.out.println("Modelo de tarefas: " + taskModel);
        System.out.println("================================");


        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}
