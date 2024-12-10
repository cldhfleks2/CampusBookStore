package com.campusbookstore.app.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MemberDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String campus;
    private Integer point;

//    private LocalDateTime createDate;
//    private LocalDateTime updateDate;
}
