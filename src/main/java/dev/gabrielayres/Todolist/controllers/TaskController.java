package dev.gabrielayres.Todolist.controllers;

import dev.gabrielayres.Todolist.tasks.TaskModel;
import dev.gabrielayres.Todolist.tasks.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("task")
public class TaskController {

    @Autowired
    private TaskRepository repository;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {


        UUID userId = (UUID) request.getAttribute("userId");
        taskModel.setUserId(userId);


        System.out.println("================================");
        System.out.println("Modelo de tarefas: " + taskModel);
        System.out.println("================================");


        repository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskModel);
    }
}
