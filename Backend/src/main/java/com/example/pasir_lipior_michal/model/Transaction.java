package com.example.pasir_lipior_michal.model;

//import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Encja Transaction reprezentuje pojedyncza transakcję finansową.
 * Każda transakcja ma unikalny identyfikator, kwotę, typ, tagi, notatki oraz datę utworzenia.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount; // Kwota transakcji

    @Enumerated(EnumType.STRING)
    private TransactionType type; // Typ transakcji (INCOME lub EXPENSE)

    private String tags; // Lista tagów lub pojedynczy tag (dla uproszczenia jako String)
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    private String notes; // Dodatkowe notatki


    private LocalDateTime timestamp; // Data i czas utworzenia transakcji

    public Transaction(Double amount, TransactionType type, String tags, String notes,User user, LocalDateTime timestamp) {
        this.amount = amount;
        this.type = type;
        this.tags = tags;
        this.notes = notes;
        this.user=user;
        this.timestamp = timestamp;
    }
    /* Konstruktor bezparametrowy
    public Transaction() {
    }

    // Konstruktor z parametrami (bez ID)
    public Transaction(Double amount, TransactionType type, String tags, String notes, LocalDateTime timestamp) {
        this.amount = amount;
        this.type = type;
        this.tags = tags;
        this.notes = notes;
        this.timestamp = timestamp;
    }

    // Getter dla ID
    public Long getId() {
        return id;
    }

    // Gettery i settery dla pozostałych pól
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }*/
}