package com.infosys.customer_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infosys.customer_service.entity.Customer;
import com.infosys.customer_service.repository.CustomerRepo;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerRepo repo;

    @PostMapping("/add")
    public Customer addCustomer(@RequestBody Customer customer) {
        return repo.save(customer);
    }

    @GetMapping("/all")
    public List<Customer> getAllCustomers() {
        return (List<Customer>) repo.findAll();
    }

    @GetMapping("/{custId}")
    public Customer getCustomer(@PathVariable Integer custId) {
        return repo.findById(custId).orElse(null);
    }

    @PutMapping("/update")
    public Customer updateCustomer(@RequestBody Customer customer) {
        return repo.save(customer);
    }

    @DeleteMapping("/delete/{custId}")
    public String deleteCustomer(@PathVariable Integer custId) {
        repo.deleteById(custId);
        return "Customer deleted: " + custId;
    }
}