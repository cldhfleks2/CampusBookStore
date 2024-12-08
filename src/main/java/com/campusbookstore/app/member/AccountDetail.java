package com.campusbookstore.app.member;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//유저 정보 객체
@Getter
@Setter
public class AccountDetail extends User {
    private String name;
    private String role; //authorities에서 꺼내 써도되는데 편하게.

    public AccountDetail(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
