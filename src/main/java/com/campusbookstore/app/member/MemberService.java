package com.campusbookstore.app.member;

import com.campusbookstore.app.error.ErrorService;
import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostRepository;
import com.campusbookstore.app.post.PostService;
import com.campusbookstore.app.purchase.Purchase;
import com.campusbookstore.app.purchase.PurchaseRepository;
import com.campusbookstore.app.purchase.PurchaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final PurchaseRepository purchaseRepository;

    //1. Entity -> DTO
    public MemberDTO getMemberDTO(Member member) {
        if (member == null) return null;
        MemberDTO.MemberDTOBuilder builder = MemberDTO.builder();

        if (member.getId() != null)
            builder.id(member.getId());
        if (member.getName() != null && !member.getName().isEmpty())
            builder.name(member.getName());
        if(member.getEmail() != null && !member.getEmail().isEmpty())
            builder.email(member.getEmail());
        if(member.getPhone() != null && !member.getPhone().isEmpty())
            builder.phone(member.getPhone());
        if (member.getCampus() != null && !member.getCampus().isEmpty())
            builder.campus(member.getCampus());
        if (member.getPoint() != null)
            builder.point(member.getPoint());

        return builder.build();
    }
    //2. DTO -> Entity
    public Member convertToMember(MemberDTO memberDTO) {
        if (memberDTO == null) return null;

        Member member = new Member();

        if (memberDTO.getId() != null)
            member.setId(memberDTO.getId());
        if (memberDTO.getName() != null && !memberDTO.getName().isEmpty())
            member.setName(memberDTO.getName());
        if (memberDTO.getEmail() != null && !memberDTO.getEmail().isEmpty())
            member.setEmail(memberDTO.getEmail());
        if (memberDTO.getPhone() != null && !memberDTO.getPhone().isEmpty())
            member.setPhone(memberDTO.getPhone());
        if (memberDTO.getCampus() != null && !memberDTO.getCampus().isEmpty())
            member.setCampus(memberDTO.getCampus());
        if (memberDTO.getPoint() != null)
            member.setPoint(memberDTO.getPoint());

        return member;
    }



    //뷰
    String viewLogin (Authentication auth) {
        //인증정보가 없거나 로그인됬으면 메인 화면으로 이동
        if(auth != null && auth.isAuthenticated()) return "redirect:/main";
        return "member/login";
    }
    String viewRegister (Authentication auth) {
        if(auth != null && auth.isAuthenticated()) return "redirect:/main";
        return "member/register";
    }
    String viewMyPage (Model model, Authentication auth, Integer pageIdx) {
        Optional<Member> member = memberRepository.findByName(auth.getName());
        if(!member.isPresent()) {
            return ErrorService.send(
                    HttpStatus.UNAUTHORIZED.value(),
                    "/mypage",
                    "DB없는 회원",
                    String.class
            );
        }
        //유저 정보 전달
        Long memberId = member.get().getId(); //현재 사용자의 id
        MemberDTO memberDTO = getMemberDTO(member.get());
        memberDTO.setId(null); //필요없는 값은 가림
        model.addAttribute("member", memberDTO);

        //pagination적용
        if(pageIdx == null) { pageIdx = 1; }

        //판매 내역 전달
        Page<Post> posts = postRepository.findAllByMemberId(memberId, PageRequest.of(pageIdx - 1, 2));
        model.addAttribute("sellTotalPages", posts.getTotalPages());
        model.addAttribute("sellCurrentPage", pageIdx);
        model.addAttribute("posts", posts.getContent());

        //주문 내역 전달
        Page<Purchase> purchases = purchaseRepository.findAllByMemberId(memberId, PageRequest.of(pageIdx - 1, 2));
        System.out.println( "1111111111=      " + purchases.getContent().size());
        model.addAttribute("purchaseTotalPages", purchases.getTotalPages());
        model.addAttribute("purchaseCurrentPage", pageIdx);
        model.addAttribute("purchases", purchases.getContent());
        return "member/myPage";
    }
    String viewLike(){
        return "like/like";
    }

    //회원가입
    @Transactional
    String register (Member member) {
        //비밀번호 암호화
        String passwordEncoded = passwordEncoder.encode(member.getPassword());
        member.setPassword(passwordEncoded);
        //DB저장
        memberRepository.save(member);
        return "redirect:/main";
    }
    //TODO 개인정보 수정 을 받는 곳
    //아직 미완성
    String editMypage(MemberDTO memberDTO) {
        return "success";
    }

}
