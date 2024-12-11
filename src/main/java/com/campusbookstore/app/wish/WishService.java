package com.campusbookstore.app.wish;

import com.campusbookstore.app.error.ErrorService;
import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.member.MemberRepository;
import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishService {
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    //Entity -> DTO
    WishDTO getWishDTO(Wish wish) {
        if(wish == null) return null;
        WishDTO.WishDTOBuilder wishDTOBuilder = WishDTO.builder();

        if(wish.getId() != null)
            wishDTOBuilder.id(wish.getId());
        if(wish.getQuantity() != null)
            wishDTOBuilder.quantity(wish.getQuantity());
        if(wish.getCreateDate() != null)
            wishDTOBuilder.createDate(wish.getCreateDate());
        if(wish.getPost() != null)
            wishDTOBuilder.post(wish.getPost());
        if(wish.getMember() != null)
            wishDTOBuilder.member(wish.getMember());

        return wishDTOBuilder.build();
    }

    Wish convertToWish(WishDTO wishDTO) {
        if(wishDTO == null) return null;
        Wish wish = new Wish();
        if(wishDTO.getId() != null)
            wish.setId(wishDTO.getId());
        if(wishDTO.getQuantity() != null)
            wish.setQuantity(wishDTO.getQuantity());
        if(wishDTO.getCreateDate() != null)
            wish.setCreateDate(wishDTO.getCreateDate());
        if(wishDTO.getPost() != null)
            wish.setPost(wishDTO.getPost());
        if(wishDTO.getMember() != null)
            wish.setMember(wishDTO.getMember());

        return wish;
    }

    //TODO: 장바구니 페이지
    String viewWish(Model model, Authentication auth) {
        List<Wish> wishs = wishRepository.findAllByMemberName(auth.getName());
        List<WishDTO> wishDTOs = new ArrayList<>();
        for(Wish wish : wishs) {
            WishDTO wishDTO = getWishDTO(wish);
            wishDTOs.add(wishDTO);
        }

        //TODO: 포인트정보전송
        Optional<Member> memberObj = memberRepository.findByName(auth.getName());
        //가입정보를 못찾는경우
        if(!memberObj.isPresent()) {
            return ErrorService.send(
                    HttpStatus.UNAUTHORIZED.value(),
                    "/wish",
                    "DB에 없는 회원",
                    model
            );
        }
        Member member = memberObj.get();
        model.addAttribute("point", member.getPoint());


        model.addAttribute("wishs", wishDTOs);
        return "wish/wish";
    }

    //장바구니 추가 요청
    //TODO: 요청온 갯수만큼 post에서 quantity빼서 계산 해서 다시 저장
    ResponseEntity<String> addWish(Long postId, Long quantity, Authentication auth) {
        Optional<Member> memberObj = memberRepository.findByName(auth.getName());
        if(!memberObj.isPresent()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자임");
        Member member = memberObj.get();
        
        Optional<Post> postObj = postRepository.findById(postId);
        if(!postObj.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물이 없음");
        Post post = postObj.get();
        
        //DB저장
        Wish wish = new Wish();
        wish.setQuantity(quantity);
        wish.setMember(member);
        wish.setPost(post);
        wishRepository.save(wish);

        return ResponseEntity.status(HttpStatus.CREATED).body("장바구니 추가 완료");
    }


}
