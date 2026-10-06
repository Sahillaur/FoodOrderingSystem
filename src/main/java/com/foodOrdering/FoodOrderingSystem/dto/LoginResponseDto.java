package com.foodOrdering.FoodOrderingSystem.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private int id;
    private String username;
    private String token;
    private String refreshToken;

}
