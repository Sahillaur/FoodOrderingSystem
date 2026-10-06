package com.foodOrdering.FoodOrderingSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    private int totalPrice;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "cart_food",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private List<Food> foods;
}
