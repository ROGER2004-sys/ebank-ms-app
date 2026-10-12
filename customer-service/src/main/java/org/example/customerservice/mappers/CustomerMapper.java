package org.example.customerservice.mappers;

import org.example.customerservice.DTO.CustomerRequestDTO;
import org.example.customerservice.DTO.CustomerResponseDTO;
import org.example.customerservice.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerResponseDTO toDTO(Customer customer) {
        if (customer == null) return null;
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }

    public Customer toEntity(CustomerRequestDTO dto) {
        if (dto == null) return null;
        return Customer.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }

    public void updateEntityFromDTO(CustomerRequestDTO dto, Customer customer) {
        if (dto == null || customer == null) return;
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
    }
}
