package com.foodOrdering.FoodOrderingSystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Customer {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
private String name;
private String phone;
private String email;
private String address;

    @JsonManagedReference
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    @JsonManagedReference
    @OneToOne(mappedBy = "customer")
    private Cart cart;
}
