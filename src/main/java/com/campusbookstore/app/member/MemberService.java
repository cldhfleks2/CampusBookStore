package com.campusbookstore.app.member;

import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostDTO;
import com.campusbookstore.app.post.PostRepository;
import com.campusbookstore.app.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final PostService postService;

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
    public Member convertToPost(MemberDTO memberDTO) {
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
        if(!member.isPresent()) return "error"; //현재 사용자 정보를 못찾았을때
        Long memberId = member.get().getId(); //현재 사용자의 id
        MemberDTO memberDTO = getMemberDTO(member.get());
        memberDTO.setId(null); //필요없는 값은 가림
        model.addAttribute("member", memberDTO);

        //pagination적용
        if(pageIdx == null) { pageIdx = 1; }

        Page<Post> posts = postRepository.findAllByMemberId(memberId, PageRequest.of(pageIdx - 1, 2));
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("currentPage", pageIdx);
        model.addAttribute("posts", posts.getContent());

        return "member/myPage";
    }
    String viewLike(){
        return "like/like";
    }

    //회원가입
    String register (Member member) {
        String passwordEncoded = passwordEncoder.encode(member.getPassword());
        member.setPassword(passwordEncoded);

        memberRepository.save(member);
        return "redirect:/main";
    }

    String editMypage(Member member) {
        return "success";
    }

}
