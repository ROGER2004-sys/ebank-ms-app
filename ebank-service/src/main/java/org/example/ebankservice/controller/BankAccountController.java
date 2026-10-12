package org.example.ebankservice.controller;

import jakarta.validation.Valid;
import org.example.ebankservice.DTO.BankAccountRequestDTO;
import org.example.ebankservice.DTO.BankAccountResponseDTO;
import org.example.ebankservice.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping
    public ResponseEntity<BankAccountResponseDTO> createAccount(@Valid @RequestBody BankAccountRequestDTO requestDTO) {
        return new ResponseEntity<>(bankAccountService.addAccount(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BankAccountResponseDTO>> getAllAccounts() {
        return ResponseEntity.ok(bankAccountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponseDTO> getAccountById(@PathVariable String id) {
        return ResponseEntity.ok(bankAccountService.getAccountById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BankAccountResponseDTO>> getAccountsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(bankAccountService.getAccountsByCustomerId(customerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccountResponseDTO> updateAccount(
            @PathVariable String id,
            @Valid @RequestBody BankAccountRequestDTO requestDTO) {
        return ResponseEntity.ok(bankAccountService.updateAccount(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        bankAccountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}