package com.recipe.recipe_organizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipe.recipe_organizer.entity.ShoppingList;
import com.recipe.recipe_organizer.entity.User;

public interface ShoppingListRepository
        extends JpaRepository<ShoppingList, Long> {

    List<ShoppingList> findByUser(User user);
}