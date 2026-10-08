package org.example.ebankservice.service;

import org.example.ebankservice.DTO.BankAccountRequestDTO;
import org.example.ebankservice.DTO.BankAccountResponseDTO;
import org.example.ebankservice.entity.BankAccount;
import org.example.ebankservice.feign.CustomerRestClient;
import org.example.ebankservice.mappers.BankAccountMapper;
import org.example.ebankservice.model.Customer;
import org.example.ebankservice.repository.BankAccountRepository;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional

public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final CustomerRestClient customerRestClient;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, BankAccountMapper bankAccountMapper, CustomerRestClient customerRestClient) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.customerRestClient = customerRestClient;
    }

    @Override
    @McpTool(description = "Add a new bank account")
    public BankAccountResponseDTO addAccount(BankAccountRequestDTO requestDTO) {
        try{
            customerRestClient.getCustomerById(String.valueOf(requestDTO.getCustomerId()));
            BankAccount bankAccount = bankAccountMapper.toEntity(requestDTO);
            BankAccount savedAccount = bankAccountRepository.save(bankAccount);
            return bankAccountMapper.toDTO(savedAccount);
        } catch (Exception e) {
            throw new RuntimeException("Le client avec l'ID " + requestDTO.getCustomerId() + " n'existe pas.");
        }

    }

    @Override
    @McpTool(description = "Get a bank account by its ID")
    @Transactional(readOnly = true)
    public BankAccountResponseDTO getAccountById(@McpToolParam(description = "ID of the bank account to retrieve") String id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec l'id : " + id));
        bankAccount.setCustomer(
                customerRestClient.getCustomerById(String.valueOf(bankAccount.getCustomerId())));
        return bankAccountMapper.toDTO(bankAccount);
    }

    @Override
    @Transactional(readOnly = true)
    @McpTool(description = "Get all bank accounts")
    public List<BankAccountResponseDTO> getAllAccounts() {
        return bankAccountRepository.findAll().stream()
                .map(bankAccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @McpTool(description = "Get bank accounts by customer ID")
    public List<BankAccountResponseDTO> getAccountsByCustomerId(@McpToolParam(description = "ID of the customer") Long customerId) {
        return bankAccountRepository.findByCustomerId(customerId).stream()
                .map(bankAccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @McpTool(description = "Update a bank account")
    public BankAccountResponseDTO updateAccount(@McpToolParam(description = "ID of the bank account to update") String id
            , @McpToolParam(description = "Updated bank account details") BankAccountRequestDTO requestDTO) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec l'id : " + id));

        if (requestDTO.getBalance() != null) bankAccount.setBalance(requestDTO.getBalance());
        if (requestDTO.getCurrency() != null) bankAccount.setCurrency(requestDTO.getCurrency());
        if (requestDTO.getType() != null) bankAccount.setType(requestDTO.getType());

        BankAccount updatedAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDTO(updatedAccount);
    }

    @Override
    @McpTool(description = "Delete a bank account")
    public void deleteAccount(@McpToolParam(description = "ID of the bank account to delete") String id) {
        if (!bankAccountRepository.existsById(id)) {
            throw new RuntimeException("Compte non trouvé avec l'id : " + id);
        }
        bankAccountRepository.deleteById(id);
    }
}