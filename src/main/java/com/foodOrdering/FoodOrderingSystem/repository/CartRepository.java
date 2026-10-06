package com.foodOrdering.FoodOrderingSystem.repository;

import com.foodOrdering.FoodOrderingSystem.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Integer> {

}
