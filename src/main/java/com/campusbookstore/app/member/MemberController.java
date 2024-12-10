package com.campusbookstore.app.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    
    //뷰
    @GetMapping("/login")
    String viewLogin (Authentication auth) {
        return memberService.viewLogin(auth);
    }
    @GetMapping("/register")
    String viewRegister (Authentication auth) {
        return memberService.viewRegister(auth);
    }
    @GetMapping("/mypage")
    String viewMyPage () {
        return memberService.viewMyPage();
    }
    @GetMapping("/like")
    String viewLike() {
        return memberService.viewLike();
    }

    //회원가입
    @PostMapping("/register")
    String register(Member member) {
        return memberService.register(member);
    }



}
