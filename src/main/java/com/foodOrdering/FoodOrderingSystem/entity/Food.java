package com.foodOrdering.FoodOrderingSystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String name;
    private int price;
    private String category;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @JsonBackReference
    @ManyToMany(mappedBy = "foods")
    private List<Cart> carts;

    @JsonBackReference
    @ManyToMany(mappedBy = "foods")
    private List<Order> orders;
}
