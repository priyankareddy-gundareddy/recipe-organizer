package com.recipe.recipe_organizer.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.recipe.recipe_organizer.entity.User;
import com.recipe.recipe_organizer.repository.UserRepository;
import com.recipe.recipe_organizer.security.JwtService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Register a new user
    public User registerUser(String email, String password) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email already registered");
        }

        String encryptedPassword =
                passwordEncoder.encode(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    // Login user
    public String loginUser(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"));

        boolean passwordMatches =
                passwordEncoder.matches(
                        password,
                        user.getPassword());

        if (!passwordMatches) {
            throw new IllegalArgumentException(
                    "Invalid email or password");
        }

        return jwtService.generateToken(email);
    }
}