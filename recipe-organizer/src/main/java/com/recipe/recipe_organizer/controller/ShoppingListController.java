package com.recipe.recipe_organizer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.recipe_organizer.entity.ShoppingList;
import com.recipe.recipe_organizer.service.ShoppingListService;

@RestController
@RequestMapping("/api/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(
            ShoppingListService shoppingListService) {

        this.shoppingListService = shoppingListService;
    }


    // ==========================================
    // GENERATE SHOPPING LIST FROM SELECTED RECIPES
    // ==========================================

    @PostMapping("/generate")
    public ResponseEntity<?> generateShoppingList(
            @RequestBody List<Long> recipeIds,
            Authentication authentication) {

        try {

            List<ShoppingList> shoppingList =
                    shoppingListService.generateFromRecipes(
                            recipeIds,
                            authentication.getName()
                    );

            return ResponseEntity.ok(shoppingList);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ==========================================
    // GET SHOPPING LIST
    // ==========================================

    @GetMapping
    public ResponseEntity<?> getShoppingList(
            Authentication authentication) {

        try {

            return ResponseEntity.ok(
                    shoppingListService.getShoppingList(
                            authentication.getName()
                    )
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ==========================================
    // MARK / UNMARK PURCHASED
    // ==========================================

    @PutMapping("/{id}/purchased")
    public ResponseEntity<?> updatePurchasedStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> request,
            Authentication authentication) {

        try {

            Boolean purchased =
                    request.get("purchased");

            if (purchased == null) {

                return ResponseEntity
                        .badRequest()
                        .body("purchased field is required");
            }

            ShoppingList updatedItem =
                    shoppingListService.updatePurchasedStatus(
                            id,
                            purchased,
                            authentication.getName()
                    );

            return ResponseEntity.ok(updatedItem);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ==========================================
    // DELETE SHOPPING LIST ITEM
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShoppingListItem(
            @PathVariable Long id,
            Authentication authentication) {

        try {

            shoppingListService.deleteShoppingListItem(
                    id,
                    authentication.getName()
            );

            return ResponseEntity.ok(
                    "Shopping list item deleted successfully"
            );

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}