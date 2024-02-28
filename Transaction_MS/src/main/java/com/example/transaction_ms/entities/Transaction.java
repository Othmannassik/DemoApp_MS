package com.example.transaction_ms.entities;

import com.example.transaction_ms.enums.TransactionType;
import com.example.transaction_ms.models.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Min(value = 1, message = "Amount Should be greater than 0")
    private Double amount;
    @Column(nullable = false)
    private int accountId;
    @Transient
    private Account account;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;
}
