package dev.gabrielayres.Todolist.controllers;

import dev.gabrielayres.Todolist.tasks.TaskModel;
import dev.gabrielayres.Todolist.tasks.TaskRepository;
import dev.gabrielayres.Todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
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


        System.out.println(taskModel);

        UUID userId = (UUID) request.getAttribute("userId");
        taskModel.setUserId(userId);


        System.out.println("================================");
        System.out.println("Modelo de tarefas: " + taskModel);
        System.out.println("================================");


        repository.save(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskModel);
    }

    @GetMapping("/list")
    public ResponseEntity list(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        List<TaskModel> tasks = repository.findByUserId(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(tasks);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var task = this.repository.findById(id).orElse(null);

        if(task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found");
        }

        var userId = request.getAttribute("userId");
        if(!task.getUserId().equals(userId))  {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not allowed to update this task");

        }

        Utils.copyNonNullProperties(taskModel, task);

        this.repository.save(task);

        var taskUpdated = this.repository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity delete(HttpServletRequest request, @PathVariable UUID id) {
        TaskModel task = repository.findById(id).orElse(null);

        if(task == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found");

        UUID userId = (UUID) request.getAttribute("userId");

        System.out.println(userId);
        System.out.println(task.getUserId());

        if(!userId.equals(task.getUserId())) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't delete this task");

        repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted with success");
    }
}
