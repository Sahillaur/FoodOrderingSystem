package com.foodOrdering.FoodOrderingSystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private int quantity;
    private int totalPrice;

    private int customerId;

    private List<Integer> foodIds;
}
