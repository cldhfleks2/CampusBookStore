package com.campusbookstore.app.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String email;
    private String phone;
    private String campus;
    private String password;

    //1:회원 0:회원탈퇴
    private int status=1;
}
