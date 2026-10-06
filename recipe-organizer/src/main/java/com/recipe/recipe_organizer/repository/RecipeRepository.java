package com.recipe.recipe_organizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.recipe_organizer.entity.Recipe;
import com.recipe.recipe_organizer.entity.User;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByCategory(String category);

    List<Recipe> findByUser(User user);

    List<Recipe> findByUserAndCategory(User user, String category);
}