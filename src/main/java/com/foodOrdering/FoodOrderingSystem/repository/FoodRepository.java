package com.foodOrdering.FoodOrderingSystem.repository;

import com.foodOrdering.FoodOrderingSystem.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Integer> {
}
