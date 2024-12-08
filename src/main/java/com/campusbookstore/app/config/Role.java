package com.campusbookstore.app.config;

import lombok.Getter;

//getRoleName() 으로 값을 가져올 수 있음
@Getter
public enum Role {
    ADMIN("ADMIN"),
    VIP("VIP"),
    USER("USER");

    private final String roleName;
    //생성자
    Role(String roleName) {
        this.roleName = roleName;
    }
}
