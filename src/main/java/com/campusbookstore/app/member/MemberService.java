package com.campusbookstore.app.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //뷰
    String viewLogin () {
        return "member/login";
    }
    String viewRegister () {
        return "member/register";
    }
    String viewMyPage () {
        return "member/myPage";
    }
    String viewCart(){
        return "cart/cart";
    }
    
    //회원가입
    String register (Member member) {
        String passwordEncoded = passwordEncoder.encode(member.getPassword());
        member.setPassword(passwordEncoded);

        memberRepository.save(member);
        return "redirect:/main";
    }



}
