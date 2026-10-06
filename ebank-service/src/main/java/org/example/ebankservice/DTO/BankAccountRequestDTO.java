package org.example.ebankservice.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.example.ebankservice.enums.AccountType;
import org.example.ebankservice.enums.Currency;
import org.example.ebankservice.model.Customer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountRequestDTO {

    @NotNull(message = "Le solde initial est obligatoire")
    @Positive(message = "Le solde doit être positif")
    private Double balance;

    @NotNull(message = "La devise est obligatoire")
    private Currency currency;

    @NotNull(message = "Le type de compte est obligatoire")
    private AccountType type;

    @NotNull(message = "L'id du client est obligatoire")
    private Long customerId;

    private Customer customer;
}