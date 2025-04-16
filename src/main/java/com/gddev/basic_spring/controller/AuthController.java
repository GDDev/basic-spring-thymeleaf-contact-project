package com.gddev.basic_spring.controller;

import com.gddev.basic_spring.model.RegisterUserDTO;
import com.gddev.basic_spring.model.User;
import com.gddev.basic_spring.model.UserDTO;
import com.gddev.basic_spring.repository.UserRepository;
import com.gddev.basic_spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterUserDTO user) {
        try {
            User newUser = User.builder().username(user.username()).password(user.password()).build();
            service.newUser(newUser);
            return ResponseEntity.ok().body("Usuário cadastrado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserDTO user) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        this.authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }
}
