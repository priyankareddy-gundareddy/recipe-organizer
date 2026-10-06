package com.recipe.recipe_organizer.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.recipe.recipe_organizer.entity.Recipe;
import com.recipe.recipe_organizer.entity.ShoppingList;
import com.recipe.recipe_organizer.entity.User;
import com.recipe.recipe_organizer.repository.RecipeRepository;
import com.recipe.recipe_organizer.repository.ShoppingListRepository;
import com.recipe.recipe_organizer.repository.UserRepository;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public ShoppingListService(
            ShoppingListRepository shoppingListRepository,
            RecipeRepository recipeRepository,
            UserRepository userRepository) {

        this.shoppingListRepository = shoppingListRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }


    // ==========================================
    // GENERATE SHOPPING LIST FROM SELECTED RECIPES
    // ==========================================

    public List<ShoppingList> generateFromRecipes(
            List<Long> recipeIds,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));


        if (recipeIds == null || recipeIds.isEmpty()) {

            throw new IllegalArgumentException(
                    "Please select at least one recipe");
        }


        /*
         * LinkedHashMap is used so that:
         *
         * 1. Duplicate ingredients are merged
         * 2. Original ingredient order is maintained
         */

        Map<String, String> uniqueIngredients =
                new LinkedHashMap<>();


        for (Long recipeId : recipeIds) {

            Recipe recipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Recipe not found: " + recipeId));


            // Make sure the recipe belongs to logged-in user

            if (recipe.getUser() == null ||
                    !recipe.getUser().getId()
                            .equals(user.getId())) {

                throw new IllegalArgumentException(
                        "Access denied");
            }


            String ingredients =
                    recipe.getIngredients();


            if (ingredients == null ||
                    ingredients.trim().isEmpty()) {

                continue;
            }


            // Ingredients are stored comma-separated

            String[] items =
                    ingredients.split(",");


            for (String item : items) {

                String cleanedItem =
                        item.trim();


                if (!cleanedItem.isEmpty()) {

                    /*
                     * Convert to lowercase only for checking duplicates,
                     * but keep the original text for display.
                     */

                    String key =
                            cleanedItem.toLowerCase();


                    uniqueIngredients.putIfAbsent(
                            key,
                            cleanedItem
                    );
                }
            }
        }


        if (uniqueIngredients.isEmpty()) {

            throw new IllegalArgumentException(
                    "Selected recipes have no ingredients");
        }


        // Create shopping-list records

        List<ShoppingList> shoppingItems =
                new ArrayList<>();


        for (String ingredient :
                uniqueIngredients.values()) {

            ShoppingList shoppingList =
                    new ShoppingList(
                            ingredient,
                            user
                    );

            shoppingItems.add(shoppingList);
        }


        return shoppingListRepository
                .saveAll(shoppingItems);
    }


    // ==========================================
    // GET SHOPPING LIST
    // ==========================================

    public List<ShoppingList> getShoppingList(
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"));

        return shoppingListRepository.findByUser(user);
    }


    // ==========================================
    // MARK / UNMARK PURCHASED
    // ==========================================

    public ShoppingList updatePurchasedStatus(
            Long id,
            boolean purchased,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"));


        ShoppingList item =
                shoppingListRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Shopping list item not found"));


        // Make sure item belongs to logged-in user

        if (item.getUser() == null ||
                !item.getUser().getId()
                        .equals(user.getId())) {

            throw new IllegalArgumentException(
                    "Access denied");
        }


        item.setPurchased(purchased);

        return shoppingListRepository.save(item);
    }


    // ==========================================
    // DELETE SHOPPING-LIST ITEM
    // ==========================================

    public void deleteShoppingListItem(
            Long id,
            String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"));


        ShoppingList item =
                shoppingListRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Shopping list item not found"));


        // Make sure item belongs to logged-in user

        if (item.getUser() == null ||
                !item.getUser().getId()
                        .equals(user.getId())) {

            throw new IllegalArgumentException(
                    "Access denied");
        }


        shoppingListRepository.delete(item);
    }
}