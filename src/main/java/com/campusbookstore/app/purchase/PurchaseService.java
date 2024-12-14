package com.campusbookstore.app.purchase;

import com.campusbookstore.app.error.ErrorService;
import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.member.MemberRepository;
import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostRepository;
import com.campusbookstore.app.wish.Wish;
import com.campusbookstore.app.wish.WishRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final WishRepository wishRepository;

    //주문하기
    //게시물 수량 체크해서 변경하고 장바구니 데이터 지우고 주문정보 DB 저장
    @Transactional
    ResponseEntity<String> order(List<PurchaseDTO> purchaseDTOs, Authentication auth) {
        //유효성 검사
        Optional<Member> memberObj = memberRepository.findByName(auth.getName());
        //1. 사용자 정보 확인
        if(!memberObj.isPresent()) return ErrorService.send(HttpStatus.UNAUTHORIZED.value(), "/order", "사용자 정보가 존재 하지 않습니다.", ResponseEntity.class);
        Member member = memberObj.get();
        //주문 목록마다 order 생성
        for(PurchaseDTO purchaseDTO : purchaseDTOs) {
            System.out.println("System.        " + purchaseDTO.getWishId());
            Optional<Wish> wishObj = wishRepository.findById(purchaseDTO.getWishId());
            //2. 장바구니에 존재하는지 확인
            if(!wishObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/order", "장바구니 항목이 존재 하지 않습니다.", ResponseEntity.class);
                continue; //존재하지 않으면 건너띔
            }
            Wish wish = wishObj.get();
            Optional<Post> postObj = postRepository.findById(wish.getPost().getId());
            //3. 게시물 확인
            if(!postObj.isPresent()){
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/order", "게시물이 존재 하지 않습니다.", ResponseEntity.class);
                continue;
            }
            Post post = postObj.get();
            //4. 게시물의 수량이 부족한경우 (혹시 모를 에러 처리)
            if(post.getQuantity() < purchaseDTO.getQuantity()){
                ErrorService.send(HttpStatus.CONFLICT.value(), "/order", "수량이 부족합니다.", ResponseEntity.class);
                continue;
            }
            // order DB 저장
            Purchase purchase = new Purchase();
            purchase.setQuantity(purchaseDTO.getQuantity());
            purchase.setMember(member);
            purchase.setPost(post);
            purchaseRepository.save(purchase);

            // 게시물 수량 변경
            post.setQuantity(post.getQuantity() - purchaseDTO.getQuantity());
            postRepository.save(post);

            // 장바구니 데이터 삭제
            wishRepository.delete(wish);

            // 사용자 포인트 차감
            Long point = Long.valueOf(post.getPrice()) * purchaseDTO.getQuantity();
            member.setPoint(Integer.valueOf(String.valueOf(Long.valueOf(member.getPoint()) - point)));
            memberRepository.save(member);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
