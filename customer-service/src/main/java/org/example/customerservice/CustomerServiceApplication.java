package org.example.customerservice;

import org.example.customerservice.entity.Customer;
import org.example.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            // Création des 4 clients
            Customer mehdi = Customer.builder()
                    .firstName("Mehdi")
                    .lastName("EZZAHRAOUI")
                    .email("mehdi@gmail.com")
                    .phone("0600000001")
                    .build();

            Customer mohamed = Customer.builder()
                    .firstName("Mohamed")
                    .lastName("EZZAHRAOUI")
                    .email("mohamed@gmail.com")
                    .phone("0600000002")
                    .build();

            Customer yasser = Customer.builder()
                    .firstName("Yasser")
                    .lastName("EZZAHRAOUI")
                    .email("yasser@gmail.com")
                    .phone("0600000003")
                    .build();

            Customer najat = Customer.builder()
                    .firstName("Najat")
                    .lastName("EZZAHRAOUI")
                    .email("najat@gmail.com")
                    .phone("0600000004")
                    .build();

            // Enregistrement dans la base de données
            customerRepository.saveAll(List.of(mehdi, mohamed, yasser, najat));

            // Affichage dans la console pour vérifier
            customerRepository.findAll().forEach(customer -> {
                System.out.println("Customer ajouté : " + customer.getFirstName() + " (" + customer.getEmail() + ")");
            });
        };
    }
}