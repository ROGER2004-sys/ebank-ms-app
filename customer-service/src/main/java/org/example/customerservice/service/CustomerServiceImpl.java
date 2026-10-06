package org.example.customerservice.service;


import org.example.customerservice.DTO.CustomerRequestDTO;
import org.example.customerservice.DTO.CustomerResponseDTO;
import org.example.customerservice.entity.Customer;
import org.example.customerservice.mappers.CustomerMapper;
import org.example.customerservice.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@RestController
@RequestMapping("/api/customers")
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        if (customerRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Un client avec cet email existe déjà.");
        }
        Customer customer = customerMapper.toEntity(requestDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDTO(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id : " + id));
        return customerMapper.toDTO(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id : " + id));

        customerMapper.updateEntityFromDTO(requestDTO, customer);
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toDTO(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Client non trouvé avec l'id : " + id);
        }
        customerRepository.deleteById(id);
    }
}