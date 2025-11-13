package com.uniClub.enums;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("ADMİN"),
    USER("Kullanıcı"),
    CLUB_MANAGER("Kulüp Yönetici");

    private final String label;

    Role(String label) {
        this.label = label;
    }
}
