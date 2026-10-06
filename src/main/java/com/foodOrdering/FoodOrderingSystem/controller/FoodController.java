package com.foodOrdering.FoodOrderingSystem.controller;

import com.foodOrdering.FoodOrderingSystem.dto.FoodDto;
import com.foodOrdering.FoodOrderingSystem.entity.Food;
import com.foodOrdering.FoodOrderingSystem.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class FoodController {

    private FoodService foodService;
    public FoodController(FoodService foodService){
        this.foodService=foodService;
    }
    @PostMapping("/ffood")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Food> add(@RequestBody FoodDto foodDto){
        if (foodDto.getName()==null || foodDto.getName().isEmpty()
                || foodDto.getPrice() <= 0){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(foodService.addFood(foodDto));
    }
    @PutMapping("/fupdate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Food> update(@RequestBody FoodDto foodDto, @PathVariable int id){
        if (foodDto.getName()==null || foodDto.getName().isEmpty()
                || foodDto.getPrice() <= 0){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(foodService.updateFood(foodDto,id));
    }
    @DeleteMapping("/fdeletedbyid/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String del(@PathVariable int id){
        return foodService.deleteFood(id);
    }
    @GetMapping("/fgetall")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Food>> getall(){
        return ResponseEntity.ok(foodService.getAll());
    }
    @GetMapping("fgetbyid/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Food> getid(@PathVariable int id){
        return ResponseEntity.ok(foodService.getid(id));
    }
}
