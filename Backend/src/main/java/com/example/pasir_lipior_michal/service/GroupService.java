package com.example.pasir_lipior_michal.service;

import com.example.pasir_lipior_michal.dto.GroupDTO;
import com.example.pasir_lipior_michal.model.Group;
import com.example.pasir_lipior_michal.model.Membership;
import com.example.pasir_lipior_michal.model.User;
import com.example.pasir_lipior_michal.repository.GroupRepository;
import com.example.pasir_lipior_michal.repository.MembershipRepository;
// UserRepository nie jest używany bezpośrednio, ale membershipService może go używać.
// import com.example.pasir_lipior_michal.repository.UserRepository;
import com.example.pasir_lipior_michal.repository.DebtRepository; // Import jest już obecny, co jest dobre
import jakarta.persistence.EntityNotFoundException;
import org.springframework.graphql.data.method.annotation.MutationMapping;
// SecurityContextHolder nie jest tu bezpośrednio używany, ale membershipService może go używać
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final MembershipRepository membershipRepository;
    private final MembershipService membershipService;
    private final DebtRepository debtRepository; // <--- ZADEKLARUJ POLE

    // ZAKTUALIZUJ KONSTRUKTOR
    public GroupService(GroupRepository groupRepository,
                        MembershipRepository membershipRepository,
                        MembershipService membershipService,
                        DebtRepository debtRepository) { // <--- DODAJ DO KONSTRUKTORA
        this.groupRepository = groupRepository;
        this.membershipRepository = membershipRepository;
        this.membershipService = membershipService;
        this.debtRepository = debtRepository; // <--- PRZYPISZ W KONSTRUKTORZE
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @MutationMapping
    public Group createGroup(GroupDTO groupDTO) {
        User owner = membershipService.getCurrentUser(); // pobieramy aktualnie zalogowanego
        Group group = new Group();
        group.setName(groupDTO.getName());
        group.setOwner(owner);

        Group savedGroup = groupRepository.save(group);
        Membership membership = new Membership();
        membership.setUser(owner);
        membership.setGroup(savedGroup);
        membershipRepository.save(membership);
        return savedGroup;
    }

    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grupa o ID " + id + " nie istnieje."));

        // Usuń powiązania
        // Upewnij się, że DebtRepository ma metodę findByGroupId lub odpowiednią
        // oraz że MembershipRepository ma metodę findByGroupId
        debtRepository.deleteAll(debtRepository.findByGroupId(id)); // <--- TERAZ POWINNO DZIAŁAĆ
        membershipRepository.deleteAll(membershipRepository.findByGroupId(id));

        // Usuń grupę
        groupRepository.delete(group);
    }
}