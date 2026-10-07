package com.recipe.recipe_organizer.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.recipe_organizer.entity.User;
import com.recipe.recipe_organizer.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody User user) {

        try {
            User registeredUser =
                    userService.registerUser(
                            user.getEmail(),
                            user.getPassword()
                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message",
                            "User registered successfully",
                            "email",
                            registeredUser.getEmail()
                    ));

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error",
                            e.getMessage()
                    ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestBody User user) {

        try {
            String token =
                    userService.loginUser(
                            user.getEmail(),
                            user.getPassword()
                    );

            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Login successful",
                            "token",
                            token
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error",
                            e.getMessage()
                    ));
        }
    }
}