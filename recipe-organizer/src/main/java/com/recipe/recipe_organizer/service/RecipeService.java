package com.recipe.recipe_organizer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.recipe.recipe_organizer.entity.Recipe;
import com.recipe.recipe_organizer.entity.User;
import com.recipe.recipe_organizer.repository.RecipeRepository;
import com.recipe.recipe_organizer.repository.UserRepository;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(
            RecipeRepository recipeRepository,
            UserRepository userRepository) {

        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    // Add recipe for logged-in user
    public Recipe addRecipe(Recipe recipe, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        recipe.setUser(user);

        return recipeRepository.save(recipe);
    }

    // Get recipes of logged-in user
    public List<Recipe> getRecipesByUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        return recipeRepository.findByUser(user);
    }

    // Get one recipe belonging to logged-in user
    public Recipe getRecipeById(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Recipe not found"));

        if (recipe.getUser() == null ||
                !recipe.getUser().getId().equals(user.getId())) {

            throw new IllegalArgumentException("Access denied");
        }

        return recipe;
    }

    // Update own recipe
    public Recipe updateRecipe(
            Long id,
            Recipe updatedRecipe,
            String email) {

        Recipe existingRecipe = getRecipeById(id, email);

        existingRecipe.setTitle(updatedRecipe.getTitle());
        existingRecipe.setDescription(updatedRecipe.getDescription());
        existingRecipe.setIngredients(updatedRecipe.getIngredients());
        existingRecipe.setInstructions(updatedRecipe.getInstructions());
        existingRecipe.setCategory(updatedRecipe.getCategory());

        return recipeRepository.save(existingRecipe);
    }

    // Delete own recipe
    public void deleteRecipe(Long id, String email) {

        Recipe recipe = getRecipeById(id, email);

        recipeRepository.delete(recipe);
    }

    // Get own recipes by category
    public List<Recipe> getRecipesByCategory(
            String category,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        return recipeRepository.findByUserAndCategory(user, category);
    }
}