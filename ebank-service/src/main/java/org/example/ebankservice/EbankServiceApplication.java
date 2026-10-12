package org.example.ebankservice;

import org.example.ebankservice.entity.BankAccount;
import org.example.ebankservice.enums.AccountType;
import org.example.ebankservice.enums.Currency;
import org.example.ebankservice.repository.BankAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients //obligatoire pour activer Feign dans l'application
public class EbankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BankAccountRepository bankAccountRepository) {
        return args -> {
            // Boucle pour générer 10 comptes bancaires attribués aux clients (customerId de 1 à 4)
            for (int i = 1; i <= 10; i++) {
                BankAccount account = BankAccount.builder()
                        .id(UUID.randomUUID().toString())
                        .createdAt(new Date())
                        .balance(2000.0 + Math.random() * 80000)
                        .currency(i % 2 == 0 ? Currency.MAD : Currency.EUR)
                        .type(i % 3 == 0 ? AccountType.SAVINGS_ACCOUNT : AccountType.CURRENT_ACCOUNT)
                        .customerId((long) ((i % 4) + 1))
                        .build();

                bankAccountRepository.save(account);
            }

            // Affichage dans la console
            System.out.println("--- LISTE DES COMPTES CRÉÉS ---");
            bankAccountRepository.findAll().forEach(acc ->
                    System.out.println("ID: " + acc.getId() + " | Solde: " + acc.getBalance() + " " + acc.getCurrency() + " | ClientId: " + acc.getCustomerId())
            );
        };
    }
}