package dev.gabrielayres.Todolist.controllers;

import dev.gabrielayres.Todolist.infra.security.TokenService;
import dev.gabrielayres.Todolist.users.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
//@CrossOrigin
@RequestMapping("auth")
public class AuthorizationController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity create(@RequestBody RegisterDTO data) {
        if(this.repository.findByUsername(data.name()) != null) {
            System.out.println("Email already in use");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use!");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());

        UserModel newUser = new UserModel(data.username(), data.name(), encryptedPassword, data.telephone(), data.groups(), data.role());

        var userCreated = this.repository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());

        var auth  = this.authenticationManager.authenticate(usernamePassword);
        UserModel user = (UserModel) auth.getPrincipal();
        var token = tokenService.generateToken(user, data.remember());

        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("name", user.getName());
        userResponse.put("email", user.getUsername());
        userResponse.put("phone", user.getTelephone());
        userResponse.put("groups", user.getGroups());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user", userResponse);
        responseData.put("token", token);

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping("/validate")
    public ResponseEntity validate(HttpServletRequest request) {
        UserModel user = (UserModel) request.getAttribute("userData");


        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("name", user.getName());
        userResponse.put("email", user.getUsername());
        userResponse.put("phone", user.getTelephone());
        userResponse.put("groups", user.getGroups());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user", userResponse);

        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
