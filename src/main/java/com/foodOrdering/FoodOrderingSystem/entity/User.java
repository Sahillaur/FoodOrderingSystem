package com.foodOrdering.FoodOrderingSystem.entity;

import com.foodOrdering.FoodOrderingSystem.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

}
