package com.example.pasir_lipior_michal.controller;
import com.example.pasir_lipior_michal.dto.BalanceDto;
import com.example.pasir_lipior_michal.dto.TransactionDTO;
import com.example.pasir_lipior_michal.model.Transaction;
import com.example.pasir_lipior_michal.model.User;
import com.example.pasir_lipior_michal.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TransactionGraphQLController {

    private final TransactionService transactionService;

    public TransactionGraphQLController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.getAllTransactions();
    }

    @MutationMapping
    public Transaction addTransaction(@Valid @Argument TransactionDTO transactionDTO) {
        return transactionService.createTransaction(transactionDTO);
    }

    @MutationMapping
    public Transaction updateTransaction(
            @Argument Long id,
            @Valid @Argument TransactionDTO transactionDTO
    ) {
        return transactionService.updateTransaction(id, transactionDTO);
    }
    @MutationMapping
    public boolean deleteTransaction(@Argument Long id) {
        transactionService.deleteTransaction(id);
        return true;
    }
    @QueryMapping
    public BalanceDto userBalance(@Argument Double days) {
        System.out.println("Odebrano days: " + days); // log do testów
        return transactionService.getUserBalance(transactionService.getCurrentUser(), days);
    }
}