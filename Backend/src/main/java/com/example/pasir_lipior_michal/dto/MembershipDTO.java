package com.example.pasir_lipior_michal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MembershipDTO {

    @NotNull(message = "Email użytkownika nie może być puste")
    private String userEmail;

    @NotNull(message = "ID grupy nie może być puste")
    private Long groupId;
}