package com.example.pasir_lipior_michal.repository;

import com.example.pasir_lipior_michal.model.Transaction;
import com.example.pasir_lipior_michal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);

    List<Transaction> findByUser(User user);

    List<Transaction> findByUserAndTimestampAfter(User user, LocalDateTime since);
}
