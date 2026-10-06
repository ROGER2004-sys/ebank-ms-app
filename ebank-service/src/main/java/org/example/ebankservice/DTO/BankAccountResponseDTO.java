package org.example.ebankservice.DTO;

import lombok.*;
import org.example.ebankservice.enums.AccountType;
import org.example.ebankservice.enums.Currency;
import org.example.ebankservice.model.Customer;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountResponseDTO {

    private String id;
    private Date createdAt;
    private Double balance;
    private Currency currency;
    private AccountType type;
    private Long customerId;
    private Customer customer;
}