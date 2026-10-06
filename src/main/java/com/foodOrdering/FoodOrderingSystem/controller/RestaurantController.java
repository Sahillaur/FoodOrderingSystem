package com.foodOrdering.FoodOrderingSystem.controller;

import com.foodOrdering.FoodOrderingSystem.dto.RestaurantDto;
import com.foodOrdering.FoodOrderingSystem.entity.Restaurant;
import com.foodOrdering.FoodOrderingSystem.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.List;

@RestController
@RequestMapping
public class RestaurantController {
    private RestaurantService restaurantService;
    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService=restaurantService;
    }

    @PostMapping("/restaurant")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Restaurant> add(@RequestBody RestaurantDto restaurantDto){
        if (restaurantDto.getName() == null || restaurantDto.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(restaurantService.restaurantData(restaurantDto));
    }

    @GetMapping("/rgetall")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Restaurant>> get(){
        return ResponseEntity.ok(restaurantService.getData());
    }

    @GetMapping("/rgetbyid/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Restaurant> getid(@PathVariable int id) {
        try {
            return ResponseEntity.ok(restaurantService.getDataId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/rput/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Restaurant> put(@RequestBody RestaurantDto restaurantDto, @PathVariable int id){
        if (restaurantDto.getName() == null || restaurantDto.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(restaurantService.updateData(restaurantDto,id));
    }

    @DeleteMapping("/rdeletebyid/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String del(@PathVariable int id){
        return restaurantService.deleteData(id);
    }

}
