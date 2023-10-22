package br.com.todoist.todoist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.todoist.todoist.user.AuthenticationDTO;
import br.com.todoist.todoist.user.UserModel;
import br.com.todoist.todoist.user.UserRepository;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired 
    private UserRepository repository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserModel data) {
        if(this.repository.findByEmail(data.getEmail()) != null) return ResponseEntity.badRequest().build();

        System.out.println("Olá mundo");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
        UserModel newUser = new UserModel(data.getEmail(), data.getName(), encryptedPassword);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
