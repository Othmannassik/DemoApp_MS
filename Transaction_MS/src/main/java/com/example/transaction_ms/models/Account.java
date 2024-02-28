package com.example.transaction_ms.models;

import java.time.LocalDateTime;

public record Account (
        Double balance,
        Client client,
        LocalDateTime createdAt
){
}
