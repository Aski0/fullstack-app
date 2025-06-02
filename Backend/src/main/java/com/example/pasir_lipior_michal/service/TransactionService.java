//TransactionService.java
package com.example.pasir_lipior_michal.service;

import com.example.pasir_lipior_michal.dto.TransactionDTO;
import com.example.pasir_lipior_michal.model.Transaction;
import com.example.pasir_lipior_michal.model.TransactionType;
import com.example.pasir_lipior_michal.model.User;
import com.example.pasir_lipior_michal.repository.TransactionRepository;
import com.example.pasir_lipior_michal.repository.UserRepository;
import com.example.pasir_lipior_michal.dto.BalanceDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getAllTransactions() {
        User user=getCurrentUser();
        return transactionRepository.findAllByUser(user);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));
    }

    public Transaction createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());
        transaction.setUser(getCurrentUser());
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, TransactionDTO transactionDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));
        if(!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new SecurityException("Brak dostępu do edycji tej transakcji");
        }
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setTags(transactionDTO.getTags());
        transaction.setNotes(transactionDTO.getNotes());

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono transakcji o ID " + id));
        if(!transaction.getUser().getEmail().equals(getCurrentUser().getEmail())){
            throw new SecurityException("Brak dostępu do edycji tej transakcji");
        }
        transactionRepository.delete(transaction);
    }
    public User getCurrentUser(){
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("Nie znaleziono zalogowanego użytkownika"));

    }
    public BalanceDto getUserBalance(User user, Double days) {
        List<Transaction> userTransactions;

        if (days != null) {
            long seconds = (long)(days * 24 * 60 * 60);
            LocalDateTime since = LocalDateTime.now().minusSeconds(seconds);
            userTransactions = transactionRepository.findByUserAndTimestampAfter(user, since);
        } else {
            userTransactions = transactionRepository.findByUser(user);
        }

        double income = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double expense = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        return new BalanceDto(income, expense, income - expense);
    }

}


