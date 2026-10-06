package com.foodOrdering.FoodOrderingSystem.controller;

import com.foodOrdering.FoodOrderingSystem.dto.OrderDto;
import com.foodOrdering.FoodOrderingSystem.entity.Order;
import com.foodOrdering.FoodOrderingSystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/orderadd")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> add(@RequestBody OrderDto orderDto) {

        if (orderDto.getTotalPrice() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                orderService.addOrder(orderDto)
        );
    }


    @PutMapping("/orderupdate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> update(
            @RequestBody OrderDto orderDto,
            @PathVariable int id) {

        if (orderDto.getTotalPrice() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                orderService.updateOrder(orderDto, id)
        );
    }


    @DeleteMapping("/deletedorder/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String del(@PathVariable int id) {

        return orderService.deleteOrder(id);
    }


    @GetMapping("/getorder")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Order>> getall() {

        return ResponseEntity.ok(
                orderService.getAll()
        );
    }


    // ================= GET ORDERS BY CUSTOMER =================

    @GetMapping("/getorderbycustomer/{customerId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Order>> getOrdersByCustomer(
            @PathVariable int customerId) {

        return ResponseEntity.ok(
                orderService.getOrdersByCustomer(customerId)
        );
    }


    @GetMapping("/getbyidorder/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Order> get(@PathVariable int id) {

        try {

            return ResponseEntity.ok(
                    orderService.getById(id)
            );

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().build();
        }
    }


    // ================= CART → ORDER =================

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/orderfromcart/{cartId}")
    public ResponseEntity<Order> createOrderFromCart(
            @PathVariable int cartId) {

        return ResponseEntity.ok(
                orderService.createOrderFromCart(cartId)
        );
    }


    // ================= CANCEL ORDER =================

    @PutMapping("/cancelorder/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable int id) {

        return ResponseEntity.ok(
                orderService.cancelOrder(id)
        );
    }
}