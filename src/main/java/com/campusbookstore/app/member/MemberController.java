package com.campusbookstore.app.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    String viewMyPage (Model model, Authentication auth, Integer pageIdx) {
        return memberService.viewMyPage(model, auth, pageIdx);
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
    @PostMapping("/editMypage")
    String editMypage(MemberDTO memberDTO) {
        return memberService.editMypage(memberDTO);
    }



}
