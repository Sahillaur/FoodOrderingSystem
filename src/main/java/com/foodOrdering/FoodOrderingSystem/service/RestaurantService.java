package com.foodOrdering.FoodOrderingSystem.service;

import com.foodOrdering.FoodOrderingSystem.dto.RestaurantDto;
import com.foodOrdering.FoodOrderingSystem.entity.Restaurant;
import com.foodOrdering.FoodOrderingSystem.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository){
        this.restaurantRepository=restaurantRepository;
    }
    public Restaurant restaurantData(RestaurantDto restaurantDto){
        Restaurant r=new Restaurant();
        r.setAddress(restaurantDto.getAddress());
        r.setPhone(restaurantDto.getPhone());
        r.setName(restaurantDto.getName());
        return restaurantRepository.save(r);
    }
    public List getData(){
        return restaurantRepository.findAll();
    }
    public Restaurant getDataId(int id){
        Optional<Restaurant> valid=restaurantRepository.findById(id);
        if (valid.isPresent()){
            return valid.get();
            }
        return valid.orElseThrow(()-> new RuntimeException("id not found"));
    }
    public Restaurant updateData(RestaurantDto restaurantDto,int id){
        Optional<Restaurant> valid=restaurantRepository.findById(id);
        if (valid.isPresent()){
            Restaurant exit=valid.get();
            exit.setName(restaurantDto.getName());
            exit.setAddress(restaurantDto.getAddress());
            exit.setPhone(restaurantDto.getPhone());
            return restaurantRepository.save(exit);
        }else{
            return valid.orElseThrow(()->new RuntimeException("id not found"));
        }
    }
    public String deleteData(int id){
        Optional<Restaurant> valid=restaurantRepository.findById(id);
        if (valid.isPresent()){
            restaurantRepository.deleteById(id);
            return "data deleted"+id;
        }else {
            return "id not found";
        }


    }
}
