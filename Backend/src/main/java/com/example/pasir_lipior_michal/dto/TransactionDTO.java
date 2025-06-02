package com.example.pasir_lipior_michal.dto;

import com.example.pasir_lipior_michal.model.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public class TransactionDTO {

    @NotNull(message = "Kwota transakcji nie może być pusta")
    @Min(value = 1, message = "Kwota transakcji musi być większa od 0")
    private Double amount;

    @NotNull(message = "Typ transakcji jest wymagany")
    private TransactionType type;

    @Size(max = 50, message = "Tagi nie mogą przekraczać 50 znaków")
    private String tags;

    @Size(max = 255, message = "Notatka może mieć maksymalnie 255 znaków")
    private String notes;

    public TransactionDTO() {
    }

    public TransactionDTO(Double amount, TransactionType type, String tags, String notes) {
        this.amount = amount;
        this.type = type;
        this.tags = tags;
        this.notes = notes;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public @NotNull(message = "Typ transakcji jest wymagany") TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
