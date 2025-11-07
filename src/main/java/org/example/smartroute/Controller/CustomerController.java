package org.example.smartroute.controller;

import org.example.smartroute.entities.DTO.customer.CreateCustomerDto;
import org.example.smartroute.entities.DTO.customer.UpdateCustomerDto;
import org.example.smartroute.entities.DTO.customer.CustomerDto;
import org.example.smartroute.services.ICustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> create(@RequestBody CreateCustomerDto dto) {
        CustomerDto created = customerService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> update(@PathVariable Long id, @RequestBody UpdateCustomerDto dto
    ) {
        CustomerDto updated = customerService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getById(@PathVariable Long id) {
        CustomerDto dto = customerService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAll() {
        List<CustomerDto> list = customerService.getAll();
        return ResponseEntity.ok(list);
    }
}
