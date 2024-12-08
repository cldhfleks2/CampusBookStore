package com.campusbookstore.app.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    String viewRegister (Authentication auth) {
        //인증정보가 없거나 로그인됬으면 메인 화면으로 이동
        if(auth != null && auth.isAuthenticated())
            return "redirect:/main";
        
        //화면 보여주기
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
