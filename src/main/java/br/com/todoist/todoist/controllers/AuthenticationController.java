package br.com.todoist.todoist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.todoist.todoist.infra.security.TokenService;
import br.com.todoist.todoist.user.AuthenticationDTO;
import br.com.todoist.todoist.user.UserModel;
import br.com.todoist.todoist.user.UserRepository;
import br.com.todoist.todoist.user.LoginResponseDTO;


@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired 
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data) {
        /* System.out.println("Data: " + data);
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        System.out.println("usernamePassword: " + usernamePassword);
        var auth = this.authenticationManager.authenticate(usernamePassword);
        System.out.println("Cheguei até aqui!");
        
        var token = tokenService.generateToken((UserModel) auth.getPrincipal()); */

        var authenticationToken = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((UserModel) authentication.getPrincipal());


        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserModel data) {
        if(this.repository.findByUsername(data.getUsername()) != null) return ResponseEntity.badRequest().build();

        System.out.println("Step 1");        
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
        UserModel newUser = new UserModel(data.getUsername(), data.getName(), encryptedPassword);
            
        var userCreated = this.repository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
