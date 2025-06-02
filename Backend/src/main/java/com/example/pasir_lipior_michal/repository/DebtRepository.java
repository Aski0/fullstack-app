package com.example.pasir_lipior_michal.repository;

import com.example.pasir_lipior_michal.model.Debt;
import com.example.pasir_lipior_michal.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findByGroupId(Long groupId);
}