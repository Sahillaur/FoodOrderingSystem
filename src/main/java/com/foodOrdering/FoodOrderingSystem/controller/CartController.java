package com.foodOrdering.FoodOrderingSystem.controller;

import com.foodOrdering.FoodOrderingSystem.dto.CartDto;
import com.foodOrdering.FoodOrderingSystem.entity.Cart;
import com.foodOrdering.FoodOrderingSystem.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @PostMapping("/cart")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Cart> add(@RequestBody CartDto cartDto) {

        if (cartDto.getQuantity() < 1 ||
                cartDto.getTotalPrice() <= 0) {

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(cartService.addCart(cartDto));
    }


    @PutMapping("/cartupdate/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Cart> update(
            @RequestBody CartDto cartDto,
            @PathVariable int id) {

        if (cartDto.getQuantity() < 1 ||
                cartDto.getTotalPrice() <= 0) {

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                cartService.updateCart(cartDto, id)
        );
    }


    @DeleteMapping("/cartdel/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String del(@PathVariable int id) {

        return cartService.del(id);
    }


    @GetMapping("/cartgetall")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Cart>> get() {

        return ResponseEntity.ok(cartService.getall());
    }


    @GetMapping("/cargetid/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Cart> get(@PathVariable int id) {

        return ResponseEntity.ok(cartService.get(id));
    }


    @GetMapping("/cartbycustomer/{customerId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Cart> getByCustomer(
            @PathVariable int customerId) {

        return ResponseEntity.ok(
                cartService.getByCustomer(customerId)
        );
    }

}