package org.example.customerservice.service;

import org.example.customerservice.DTO.CustomerRequestDTO;
import org.example.customerservice.DTO.CustomerResponseDTO;

import java.util.List;

public interface CustomerService {
    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);
    CustomerResponseDTO getCustomerById(Long id);
    List<CustomerResponseDTO> getAllCustomers();
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO);
    void deleteCustomer(Long id);
}
