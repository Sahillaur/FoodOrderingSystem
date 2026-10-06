package com.foodOrdering.FoodOrderingSystem.dto;

import com.foodOrdering.FoodOrderingSystem.enums.OrderStatus;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private int totalPrice;
    private OrderStatus status;
    private String orderDate;

    private int customerId;

    private List<Integer> foodIds;

    private int restaurantId;
}
