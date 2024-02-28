package com.example.client_account_ms.dtos;

import lombok.Builder;

@Builder
public record ClientDTO(
        int id,
        String fullName,
        String cni,
        String phone
) {
    public ClientDTO(int id, String fullName, String cni, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.cni = cni;
        this.phone = phone;
    }
}
