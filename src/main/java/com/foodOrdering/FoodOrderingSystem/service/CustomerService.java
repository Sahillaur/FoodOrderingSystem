package com.foodOrdering.FoodOrderingSystem.service;

import com.foodOrdering.FoodOrderingSystem.dto.CustomerDto;
import com.foodOrdering.FoodOrderingSystem.entity.Customer;
import com.foodOrdering.FoodOrderingSystem.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

private CustomerRepository customerRepository;
public CustomerService(CustomerRepository customerRepository){
    this.customerRepository=customerRepository;
}
public Customer customerData(CustomerDto customerDto){
    Customer c=new Customer();

    c.setName(customerDto.getName());
    c.setPhone(customerDto.getPhone());
    c.setEmail(customerDto.getEmail());
    c.setAddress(customerDto.getAddress());
    return customerRepository.save(c);
}
public List<Customer> getData(){
    return customerRepository.findAll();
}
public Customer getDataId(int id){
    Optional<Customer> valid=customerRepository.findById(id);
    if(valid.isEmpty()){
        return valid.get();
    }
    else{
        return  valid.orElseThrow(()-> new RuntimeException("id not matched"));
    }
}
public Customer updateData(CustomerDto customerDto, int id){
    Optional<Customer> valid=customerRepository.findById(id);
    if (valid.isPresent()){
        Customer exists=valid.get();
        exists.setName(customerDto.getName());
        exists.setPhone(customerDto.getPhone());
        exists.setEmail(customerDto.getEmail());
        exists.setAddress(customerDto.getAddress());
        return customerRepository.save(exists);
    }
    else {
        return valid.orElseThrow(() -> new RuntimeException("id not matched"));
    }
}
public String deleteDataId(int id){
    Optional<Customer> valid=customerRepository.findById(id);
    if(valid.isPresent()){
        customerRepository.deleteById(id);
        return " data is deleted"+id;
    }
    return "id not found";

}



}
