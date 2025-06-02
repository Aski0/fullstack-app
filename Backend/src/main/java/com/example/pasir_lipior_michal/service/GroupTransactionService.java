package com.example.pasir_lipior_michal.service;

import com.example.pasir_lipior_michal.dto.GroupTransactionDTO;
import com.example.pasir_lipior_michal.model.Debt;
import com.example.pasir_lipior_michal.model.Group;
import com.example.pasir_lipior_michal.model.Membership;
import com.example.pasir_lipior_michal.model.User;
import com.example.pasir_lipior_michal.repository.DebtRepository;
import com.example.pasir_lipior_michal.repository.GroupRepository;
import com.example.pasir_lipior_michal.repository.MembershipRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 3 usages 🔍 Kijowski
public class GroupTransactionService {

    private final GroupRepository groupRepository; // 2 usages
    private final MembershipRepository membershipRepository; // 2 usages
    private final DebtRepository debtRepository; // 2 usages

    public GroupTransactionService(GroupRepository groupRepository, MembershipRepository membershipRepository, DebtRepository debtRepository) { // 1 usage 🔍 Kijowski
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.debtRepository = debtRepository;
    }

    public void addGroupTransaction(GroupTransactionDTO dto, User currentUser) {
        Group group = groupRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy"));

        List<Membership> members = membershipRepository.findByGroupId(group.getId());
        List<Long> selectedUserIds = dto.getSelectedUserIds();

        if (selectedUserIds == null || selectedUserIds.isEmpty()) {
            throw new IllegalArgumentException("Nie wybrano żadnych użytkowników");
        }

        double amountPerUser = dto.getAmount() / selectedUserIds.size();

        for (Membership member : members) {
            User debtor = member.getUser();
            if (!debtor.getId().equals(currentUser.getId()) && selectedUserIds.contains(debtor.getId())) {
                Debt debt = new Debt();
                debt.setDebtor(debtor);
                debt.setCreditor(currentUser);
                debt.setGroup(group);
                debt.setAmount(amountPerUser);
                debt.setTitle(dto.getTitle());
                debtRepository.save(debt);
            }
        }
    }
}