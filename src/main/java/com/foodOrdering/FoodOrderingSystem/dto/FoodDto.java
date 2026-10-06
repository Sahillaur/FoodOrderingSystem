package com.foodOrdering.FoodOrderingSystem.dto;

import lombok.Data;

@Data
public class FoodDto {
    private int restaurantId;

    private String name;
    private  int price;
    private  String category;
}
