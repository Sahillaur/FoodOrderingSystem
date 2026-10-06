package com.foodOrdering.FoodOrderingSystem.repository;

import com.foodOrdering.FoodOrderingSystem.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant,Integer> {
}
