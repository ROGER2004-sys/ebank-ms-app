package org.example.ebankservice.service;

import org.example.ebankservice.DTO.BankAccountRequestDTO;
import org.example.ebankservice.DTO.BankAccountResponseDTO;
import org.example.ebankservice.entity.BankAccount;
import org.example.ebankservice.mappers.BankAccountMapper;
import org.example.ebankservice.repository.BankAccountRepository;

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

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, BankAccountMapper bankAccountMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public BankAccountResponseDTO addAccount(BankAccountRequestDTO requestDTO) {
        BankAccount bankAccount = bankAccountMapper.toEntity(requestDTO);
        BankAccount savedAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDTO(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccountResponseDTO getAccountById(String id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec l'id : " + id));
        return bankAccountMapper.toDTO(bankAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankAccountResponseDTO> getAllAccounts() {
        return bankAccountRepository.findAll().stream()
                .map(bankAccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankAccountResponseDTO> getAccountsByCustomerId(Long customerId) {
        return bankAccountRepository.findByCustomerId(customerId).stream()
                .map(bankAccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountResponseDTO updateAccount(String id, BankAccountRequestDTO requestDTO) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec l'id : " + id));

        if (requestDTO.getBalance() != null) bankAccount.setBalance(requestDTO.getBalance());
        if (requestDTO.getCurrency() != null) bankAccount.setCurrency(requestDTO.getCurrency());
        if (requestDTO.getType() != null) bankAccount.setType(requestDTO.getType());

        BankAccount updatedAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDTO(updatedAccount);
    }

    @Override
    public void deleteAccount(String id) {
        if (!bankAccountRepository.existsById(id)) {
            throw new RuntimeException("Compte non trouvé avec l'id : " + id);
        }
        bankAccountRepository.deleteById(id);
    }
}