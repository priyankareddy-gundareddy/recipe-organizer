package com.recipe.recipe_organizer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shopping_list")
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String item;

    @Column(nullable = false)
    private boolean purchased = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public ShoppingList() {
    }

    public ShoppingList(String item, User user) {
        this.item = item;
        this.user = user;
        this.purchased = false;
    }

    public Long getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}