package com.foodOrdering.FoodOrderingSystem.service;

import com.foodOrdering.FoodOrderingSystem.dto.CartDto;
import com.foodOrdering.FoodOrderingSystem.entity.Cart;
import com.foodOrdering.FoodOrderingSystem.entity.Customer;
import com.foodOrdering.FoodOrderingSystem.entity.Food;
import com.foodOrdering.FoodOrderingSystem.repository.CartRepository;
import com.foodOrdering.FoodOrderingSystem.repository.CustomerRepository;
import com.foodOrdering.FoodOrderingSystem.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private CartRepository cartRepository;
    private CustomerRepository customerRepository;
    private FoodRepository foodRepository;

    public CartService(
            CartRepository cartRepository,
            CustomerRepository customerRepository,
            FoodRepository foodRepository) {

        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.foodRepository = foodRepository;
    }


    // ================= ADD CART =================

    public Cart addCart(CartDto cartDto) {

        Cart c = new Cart();

        c.setQuantity(cartDto.getQuantity());

        c.setTotalPrice(cartDto.getTotalPrice());


        Customer customer = customerRepository
                .findById(cartDto.getCustomerId())
                .orElseThrow(() ->
                        new RuntimeException("customer not found"));


        c.setCustomer(customer);


        List<Food> foods =
                foodRepository.findAllById(cartDto.getFoodIds());

        c.setFoods(foods);


        return cartRepository.save(c);
    }


    // ================= UPDATE CART =================

    public Cart updateCart(CartDto cartDto, int id) {

        Optional<Cart> valid =
                cartRepository.findById(id);


        if (valid.isPresent()) {

            Cart exit = valid.get();

            exit.setQuantity(cartDto.getQuantity());

            exit.setTotalPrice(cartDto.getTotalPrice());


            List<Food> foods =
                    foodRepository.findAllById(cartDto.getFoodIds());

            exit.setFoods(foods);


            return cartRepository.save(exit);
        }


        return valid.orElseThrow(() ->
                new RuntimeException("id not matched"));
    }


    // ================= DELETE CART =================

    public String del(int id) {

        Optional<Cart> valid =
                cartRepository.findById(id);


        if (valid.isPresent()) {

            Cart cart = valid.get();

            Customer customer = cart.getCustomer();


            if (customer != null) {

                customer.setCart(null);

                customerRepository.save(customer);
            }


            cartRepository.deleteById(id);

            return "deleted successfully";
        }


        return "id not found";
    }


    // ================= GET ALL =================

    public List<Cart> getall() {

        return cartRepository.findAll();
    }


    // ================= GET BY ID =================

    public Cart get(int id) {

        Optional<Cart> valid =
                cartRepository.findById(id);


        if (valid.isPresent()) {

            return valid.get();
        }


        return valid.orElseThrow(() ->
                new RuntimeException("id not found"));
    }


    // ================= GET BY CUSTOMER =================

    public Cart getByCustomer(int customerId) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("customer not found"));


        Cart cart = customer.getCart();


        if (cart == null) {

            throw new RuntimeException("cart not found");
        }


        return cart;
    }

}