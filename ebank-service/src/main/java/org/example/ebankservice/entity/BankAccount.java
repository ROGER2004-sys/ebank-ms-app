package org.example.ebankservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.ebankservice.enums.AccountType;
import org.example.ebankservice.enums.Currency;
import org.example.ebankservice.model.Customer;

import java.util.Date;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    private String id; // UUID sous forme de chaîne de caractères

    private Date createdAt;
    private Double balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    // ID de la personne propriétaire (provenant du Customer-Service)
    private Long customerId;

    @Transient //exist dans la classe mais pas dans la base de données
    private Customer customer;
}