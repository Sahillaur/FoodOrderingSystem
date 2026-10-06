package com.foodOrdering.FoodOrderingSystem.controller;

import com.foodOrdering.FoodOrderingSystem.dto.LoginResponseDto;
import com.foodOrdering.FoodOrderingSystem.dto.UserDto;
import com.foodOrdering.FoodOrderingSystem.entity.User;
import com.foodOrdering.FoodOrderingSystem.security.JwtService;
import com.foodOrdering.FoodOrderingSystem.security.TokenBlacklist;
import com.foodOrdering.FoodOrderingSystem.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final TokenBlacklist tokenBlacklist;

    public UserController(UserService userService,JwtService jwtService, TokenBlacklist tokenBlacklist) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.tokenBlacklist=tokenBlacklist;
    }

    @PostMapping("/register")
    public ResponseEntity<User> add(@RequestBody UserDto userDto) {

        if (userDto.getUsername() == null || userDto.getUsername().isEmpty()
                || userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(userService.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> log(@RequestBody UserDto userDto) {

        User user = userService.login(userDto);

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        String token= jwtService.generateToken(user.getUsername(),user.getRole());

        LoginResponseDto response = new LoginResponseDto();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setToken(token);
        response.setRefreshToken(user.getRefreshToken());// refresh token ke liye

        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);

        tokenBlacklist.addToken(token);

        String username = jwtService.extractUsername(token);
        userService.removeRefreshToken(username); // token remove db se

        return ResponseEntity.ok("Logout successful");
    }
    //Ab naya endpoint banayenge jisse Access Token expire hone ke baad Refresh Token se naya Access Token milega.
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody String refreshToken) {

        try {
            String username = jwtService.extractUsername(refreshToken);

            User user = userService.getUser(username);

            if (user == null || !refreshToken.equals(user.getRefreshToken())) {
                return ResponseEntity.status(401).build();
            }

            String token = jwtService.generateToken(user.getUsername(), user.getRole());

            LoginResponseDto response = new LoginResponseDto();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setToken(token);
            response.setRefreshToken(refreshToken);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}