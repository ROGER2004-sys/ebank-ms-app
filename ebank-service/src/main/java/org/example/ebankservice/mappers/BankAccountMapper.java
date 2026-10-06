package org.example.ebankservice.mappers;
import org.example.ebankservice.DTO.BankAccountRequestDTO;
import org.example.ebankservice.DTO.BankAccountResponseDTO;
import org.example.ebankservice.entity.BankAccount;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class BankAccountMapper {

    public BankAccountResponseDTO toDTO(BankAccount bankAccount) {
        if (bankAccount == null) return null;
        return BankAccountResponseDTO.builder()
                .id(bankAccount.getId())
                .createdAt(bankAccount.getCreatedAt())
                .balance(bankAccount.getBalance())
                .currency(bankAccount.getCurrency())
                .type(bankAccount.getType())
                .customerId(bankAccount.getCustomerId())
                .build();
    }

    public BankAccount toEntity(BankAccountRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        return BankAccount.builder()
                .id(UUID.randomUUID().toString())
                .createdAt(new Date())
                .balance(requestDTO.getBalance())
                .currency(requestDTO.getCurrency())
                .type(requestDTO.getType())
                .customerId(requestDTO.getCustomerId())
                .build();
    }
}