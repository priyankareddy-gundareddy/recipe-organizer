package com.recipe.recipe_organizer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.recipe_organizer.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}