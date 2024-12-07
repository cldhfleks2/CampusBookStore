package com.campusbookstore.app.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    String viewLogin () {

        return memberService.viewLogin();
    }

    @GetMapping("/register")
    String viewRegister () {

        return memberService.viewRegister();
    }

    @GetMapping("/mypage")
    String viewMyPage () {

        return memberService.viewMyPage();
    }

}
