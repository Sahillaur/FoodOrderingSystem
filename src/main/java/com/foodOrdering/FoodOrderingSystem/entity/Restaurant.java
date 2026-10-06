package com.foodOrdering.FoodOrderingSystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String name;
    private String address;
    private String phone;

    @JsonManagedReference
    @OneToMany(mappedBy = "restaurant")
    private List<Food> foods;

    @JsonManagedReference
    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders;
}
