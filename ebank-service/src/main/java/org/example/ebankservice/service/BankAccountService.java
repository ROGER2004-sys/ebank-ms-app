package org.example.ebankservice.service;

import org.example.ebankservice.DTO.BankAccountRequestDTO;
import org.example.ebankservice.DTO.BankAccountResponseDTO;

import java.util.List;

public interface BankAccountService {
    BankAccountResponseDTO addAccount(BankAccountRequestDTO requestDTO);
    BankAccountResponseDTO getAccountById(String id);
    List<BankAccountResponseDTO> getAllAccounts();
    List<BankAccountResponseDTO> getAccountsByCustomerId(Long customerId);
    BankAccountResponseDTO updateAccount(String id, BankAccountRequestDTO requestDTO);
    void deleteAccount(String id);
}