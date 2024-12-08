package com.campusbookstore.app.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    
    //뷰
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
    @GetMapping("/cart")
    String viewCart() {
        return memberService.viewCart();
    }

    //회원가입
    @PostMapping("/register")
    String register(Member member) {
        return memberService.register(member);
    }

}
