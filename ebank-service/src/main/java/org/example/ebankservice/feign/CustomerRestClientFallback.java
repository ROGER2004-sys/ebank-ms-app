package org.example.ebankservice.feign;

import org.example.ebankservice.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerRestClientFallback implements CustomerRestClient {

    @Override
    public Customer getCustomerById(String id) {
        return Customer.builder()
                .id(id)
                .firstName("NoName")
                .lastName("Unknown")
                .email("NoEmail")
                .phone("Unknown")
                .build();
    }
}
