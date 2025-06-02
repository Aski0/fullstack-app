package com.example.pasir_lipior_michal.service;

import com.example.pasir_lipior_michal.dto.MembershipDTO;
import com.example.pasir_lipior_michal.model.Group;
import com.example.pasir_lipior_michal.model.Membership;
import com.example.pasir_lipior_michal.model.User;
import com.example.pasir_lipior_michal.repository.GroupRepository;
import com.example.pasir_lipior_michal.repository.MembershipRepository;
import com.example.pasir_lipior_michal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 1 usage 🔍 Kijowski
public class MembershipService {

    private final MembershipRepository membershipRepository; // 6 usages
    private final GroupRepository groupRepository; // 2 usages
    private final UserRepository userRepository; // 3 usages

    public MembershipService(MembershipRepository membershipRepository, GroupRepository groupRepository, UserRepository userRepository) { // 1 usage 🔍 Kijowski
        this.membershipRepository = membershipRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    public List<Membership> getGroupMembers(Long groupId) { // 1 usage 🔍 Kijowski
        return membershipRepository.findByGroupId(groupId);
    }

    public Membership addMember(MembershipDTO membershipDTO) { // 1 usage 🔍 Kijowski
        User user = userRepository.findByEmail(membershipDTO.getUserEmail())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika o emailu: " + membershipDTO.getUserEmail()));
        Group group = groupRepository.findById(membershipDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono grupy o ID: " + membershipDTO.getGroupId()));

        // WALIDACJA: sprawdź czy użytkownik już jest członkiem tej grupy
        boolean alreadyMember = membershipRepository.findByGroupId(group.getId()).stream()
                .anyMatch(membership -> membership.getUser().getId().equals(user.getId()));

        if (alreadyMember) {
            throw new IllegalStateException("Użytkownik jest już członkiem tej grupy.");
        }

        Membership membership = new Membership();
        membership.setUser(user);
        membership.setGroup(group);
        return membershipRepository.save(membership);
    }
    public void removeMember(Long membershipId) { // 1 usage 🔍 Kijowski
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new EntityNotFoundException("Członkostwo nie istnieje"));

        User currentUser = getCurrentUser(); // kto próbuje usuwać
        User groupOwner = membership.getGroup().getOwner(); // kto jest właścicielem grupy

        if (!currentUser.getId().equals(groupOwner.getId())) {
            throw new SecurityException("Tylko właściciel grupy może usuwać członków.");
        }

        membershipRepository.deleteById(membershipId);
    }

    public User getCurrentUser() { // 3 usages 🔍 Kijowski
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika: " + email));
    }
}