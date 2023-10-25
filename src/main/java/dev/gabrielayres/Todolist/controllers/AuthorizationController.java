package dev.gabrielayres.Todolist.controllers;

import dev.gabrielayres.Todolist.infra.security.TokenService;
import dev.gabrielayres.Todolist.users.AuthenticationDTO;
import dev.gabrielayres.Todolist.users.LoginResponseDTO;
import dev.gabrielayres.Todolist.users.UserModel;
import dev.gabrielayres.Todolist.users.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthorizationController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity create(@RequestBody UserModel data) {
        if(this.repository.findByUsername(data.getUsername()) != null) {
            System.out.println("Email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use!");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
        UserModel newUser = new UserModel(data.getUsername(), data.getName(), encryptedPassword, data.getRole());

        var userCreated = this.repository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());


        return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
    }
}
