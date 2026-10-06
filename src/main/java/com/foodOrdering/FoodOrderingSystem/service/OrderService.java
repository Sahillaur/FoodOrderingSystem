package com.foodOrdering.FoodOrderingSystem.service;

import com.foodOrdering.FoodOrderingSystem.dto.OrderDto;
import com.foodOrdering.FoodOrderingSystem.entity.*;
import com.foodOrdering.FoodOrderingSystem.enums.OrderStatus;
import com.foodOrdering.FoodOrderingSystem.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private FoodRepository foodRepository;
    private RestaurantRepository restaurantRepository;
    private CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        FoodRepository foodRepository,
                        RestaurantRepository restaurantRepository,
                        CartRepository cartRepository) {

        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.foodRepository = foodRepository;
        this.restaurantRepository = restaurantRepository;
        this.cartRepository = cartRepository;
    }


    public Order addOrder(OrderDto orderDto) {

        Order o = new Order();

        o.setTotalPrice(orderDto.getTotalPrice());
        o.setStatus(orderDto.getStatus());
        o.setOrderDate(orderDto.getOrderDate());


        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() ->
                        new RuntimeException("customer not found"));

        o.setCustomer(customer);


        List<Food> foods =
                foodRepository.findAllById(orderDto.getFoodIds());

        o.setFoods(foods);


        Restaurant restaurant =
                restaurantRepository.findById(orderDto.getRestaurantId())
                        .orElseThrow(() ->
                                new RuntimeException("restaurant not found"));

        o.setRestaurant(restaurant);


        return orderRepository.save(o);
    }


    public Order updateOrder(OrderDto orderDto, int id) {

        Optional<Order> valid =
                orderRepository.findById(id);


        if (valid.isPresent()) {

            Order exit = valid.get();

            exit.setTotalPrice(orderDto.getTotalPrice());
            exit.setStatus(orderDto.getStatus());
            exit.setOrderDate(orderDto.getOrderDate());


            List<Food> foods =
                    foodRepository.findAllById(orderDto.getFoodIds());

            exit.setFoods(foods);


            Restaurant restaurant =
                    restaurantRepository.findById(orderDto.getRestaurantId())
                            .orElseThrow(() ->
                                    new RuntimeException("restaurant not found"));

            exit.setRestaurant(restaurant);


            return orderRepository.save(exit);
        }


        return valid.orElseThrow(() ->
                new RuntimeException("id not found"));
    }


    public String deleteOrder(int id) {

        Optional<Order> valid =
                orderRepository.findById(id);


        if (valid.isPresent()) {

            orderRepository.deleteById(id);

            return "order deleted";
        }


        return "id not found";
    }


    public List<Order> getAll() {

        return orderRepository.findAll();
    }


    // ================= GET ORDERS BY CUSTOMER =================

    public List<Order> getOrdersByCustomer(int customerId) {

        return orderRepository.findByCustomerId(customerId);

    }


    public Order getById(int id) {

        Optional<Order> valid =
                orderRepository.findById(id);


        if (valid.isPresent()) {

            return valid.get();
        }


        return valid.orElseThrow(() ->
                new RuntimeException("id not matched"));
    }


    // ================= CART → ORDER =================

    public Order createOrderFromCart(int cartId) {

        Optional<Cart> valid =
                cartRepository.findById(cartId);


        if (valid.isPresent()) {

            Cart cart = valid.get();

            Order o = new Order();


            o.setCustomer(cart.getCustomer());

            o.setFoods(new ArrayList<>(cart.getFoods()));

            o.setTotalPrice(cart.getTotalPrice());

            o.setStatus(OrderStatus.PLACED);

            o.setOrderDate(
                    java.time.LocalDate.now().toString()
            );


            Restaurant restaurant =
                    cart.getFoods().get(0).getRestaurant();

            o.setRestaurant(restaurant);


            return orderRepository.save(o);

        } else {

            throw new RuntimeException("cart not found");
        }
    }


    // ================= CANCEL ORDER =================

    public Order cancelOrder(int id) {

        Optional<Order> valid =
                orderRepository.findById(id);


        if (valid.isPresent()) {

            Order o = valid.get();


            if (o.getStatus() == OrderStatus.PLACED) {

                o.setStatus(OrderStatus.CANCELLED);

                return orderRepository.save(o);
            }


            throw new RuntimeException(
                    "order cannot be cancelled"
            );

        } else {

            throw new RuntimeException("id not found");
        }
    }
}