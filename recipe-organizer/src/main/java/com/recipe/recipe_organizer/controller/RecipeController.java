package com.recipe.recipe_organizer.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.recipe_organizer.entity.Recipe;
import com.recipe.recipe_organizer.service.RecipeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500",
        "http://localhost:5501",
        "http://127.0.0.1:5501"
})
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<Recipe> addRecipe(
            @Valid @RequestBody Recipe recipe,
            Authentication authentication) {

        Recipe savedRecipe = recipeService.addRecipe(
                recipe,
                authentication.getName()
        );

        return ResponseEntity.status(201).body(savedRecipe);
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getRecipes(
            Authentication authentication) {

        return ResponseEntity.ok(
                recipeService.getRecipesByUser(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipeById(
            @PathVariable Long id,
            Authentication authentication) {

        try {
            return ResponseEntity.ok(
                    recipeService.getRecipeById(
                            id,
                            authentication.getName()
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody Recipe recipe,
            Authentication authentication) {

        try {
            return ResponseEntity.ok(
                    recipeService.updateRecipe(
                            id,
                            recipe,
                            authentication.getName()
                    )
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(
            @PathVariable Long id,
            Authentication authentication) {

        try {
            recipeService.deleteRecipe(
                    id,
                    authentication.getName()
            );

            return ResponseEntity.ok(
                    "Recipe deleted successfully"
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Recipe>> getRecipesByCategory(
            @PathVariable String category,
            Authentication authentication) {

        return ResponseEntity.ok(
                recipeService.getRecipesByCategory(
                        category,
                        authentication.getName()
                )
        );
    }
}