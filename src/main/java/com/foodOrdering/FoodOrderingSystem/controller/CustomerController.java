package com.foodOrdering.FoodOrderingSystem.controller;

import com.foodOrdering.FoodOrderingSystem.dto.CustomerDto;
import com.foodOrdering.FoodOrderingSystem.entity.Customer;
import com.foodOrdering.FoodOrderingSystem.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CustomerController {
    private CustomerService customerService;
    public CustomerController(CustomerService customerService){
        this.customerService=customerService;
    }

    @PostMapping("/ccustomer")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Customer> add(@RequestBody CustomerDto customerDto){
        if (customerDto.getName() == null || customerDto.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
    return ResponseEntity.ok(customerService.customerData(customerDto));
    }
    @GetMapping("/ccustomers")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<Customer>> getAll(){
        return ResponseEntity.ok(customerService.getData());
    }

    @GetMapping("/ccustomerbyid/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Customer> get(@PathVariable int id) {
        try {
            return ResponseEntity.ok(customerService.getDataId(id));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("cupdate/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Customer> update(@PathVariable int id,@RequestBody CustomerDto customerDto){
        if (customerDto.getName() == null || customerDto.getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
    return ResponseEntity.ok(customerService.updateData(customerDto,id));
    }
    @DeleteMapping("cdeletebyid/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String del(@PathVariable int id){
    return customerService.deleteDataId(id);

    }

}


