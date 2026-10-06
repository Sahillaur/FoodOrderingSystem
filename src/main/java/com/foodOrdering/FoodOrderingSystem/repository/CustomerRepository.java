package com.foodOrdering.FoodOrderingSystem.repository;

import com.foodOrdering.FoodOrderingSystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
}
