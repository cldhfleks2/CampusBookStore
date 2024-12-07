package com.campusbookstore.app.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    String viewLogin () {
        return "member/login";
    }

    String viewRegister () {
        return "member/register";
    }

    String viewMyPage () {
        return "member/myPage";
    }



}
