package com.foodOrdering.FoodOrderingSystem.repository;

import com.foodOrdering.FoodOrderingSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
