package dev.gabrielayres.Todolist.controllers;

import dev.gabrielayres.Todolist.users.UserModel;
import dev.gabrielayres.Todolist.users.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserRepository repository;

    @GetMapping("/")
    public ResponseEntity getUserData(HttpServletRequest request) {
        UUID userId = (UUID) request.getAttribute("userId");
        var user =  repository.findById(userId);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
