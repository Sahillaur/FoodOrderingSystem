package com.foodOrdering.FoodOrderingSystem.service;

import com.foodOrdering.FoodOrderingSystem.dto.UserDto;
import com.foodOrdering.FoodOrderingSystem.entity.User;
import com.foodOrdering.FoodOrderingSystem.enums.Role;
import com.foodOrdering.FoodOrderingSystem.repository.UserRepository;
import com.foodOrdering.FoodOrderingSystem.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(UserDto userDto) {
        User u = new User();
        u.setUsername(userDto.getUsername());
        u.setPassword(passwordEncoder.encode(userDto.getPassword()));
        u.setRole(Role.USER);
        return userRepository.save(u);
    }

    public User login(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername()); // token check krneke liye

        if (user != null && passwordEncoder.matches(
                userDto.getPassword(),
                user.getPassword())) {

            String refreshToken = jwtService.generateRefreshToken(user.getUsername()); //refresh token ke liye

            user.setRefreshToken(refreshToken);
            userRepository.save(user);

            return user;
        }

        return null;
    }
    // refresh token
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }
    // DB vala refresh token remove krne ke liye
    public void removeRefreshToken(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            user.setRefreshToken(null);
            userRepository.save(user);
        }
    }
}