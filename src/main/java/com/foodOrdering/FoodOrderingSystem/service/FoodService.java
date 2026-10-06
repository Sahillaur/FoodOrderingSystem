package com.foodOrdering.FoodOrderingSystem.service;

import com.foodOrdering.FoodOrderingSystem.dto.FoodDto;
import com.foodOrdering.FoodOrderingSystem.entity.Food;
import com.foodOrdering.FoodOrderingSystem.entity.Restaurant;
import com.foodOrdering.FoodOrderingSystem.repository.FoodRepository;
import java.util.List;

import com.foodOrdering.FoodOrderingSystem.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FoodService {
    private FoodRepository foodRepository;
    private RestaurantRepository restaurantRepository;

    public FoodService(FoodRepository foodRepository,
                       RestaurantRepository restaurantRepository){
        this.foodRepository = foodRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Food addFood(FoodDto foodDto){
        Food f=new Food();
        f.setName(foodDto.getName());
        f.setPrice(foodDto.getPrice());
        f.setCategory(foodDto.getCategory());

        Restaurant restaurant = restaurantRepository.findById(foodDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("restaurant not found"));

        f.setRestaurant(restaurant);
        return foodRepository.save(f);
    }
    public Food updateFood(FoodDto foodDto,int id){
        Optional<Food> valid=foodRepository.findById(id);
        if (valid.isPresent()){
            Food exit=valid.get();
            exit.setName(foodDto.getName());
            exit.setPrice(foodDto.getPrice());
            exit.setCategory(foodDto.getCategory());

            Restaurant restaurant = restaurantRepository.findById(foodDto.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("restaurant not found"));

            exit.setRestaurant(restaurant);
            return foodRepository.save(exit);
        }
        return valid.orElseThrow(()-> new RuntimeException("id not found"));
    }
    public String deleteFood(int id){
        Optional<Food> valid=foodRepository.findById(id);
        if (valid.isPresent()){
            foodRepository.deleteById(id);
            return "food deleted";
        }
        return "id not found";
    }
    public List<Food> getAll(){
        return foodRepository.findAll();
    }
    public Food getid(int id){
        Optional<Food> valid=foodRepository.findById(id);
        if (valid.isPresent()){
            return valid.get();
        }
        return valid.orElseThrow(()-> new RuntimeException("id not found"));
    }

}
