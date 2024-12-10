package com.campusbookstore.app.review;

import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    //DTO를 수정하면 아래 두개를 수정해야한다.
    //1. Entity -> DTO
    public ReviewDTO getReviewDTO(Review review) {
        if (review == null) return null;
        ReviewDTO.ReviewDTOBuilder builder = ReviewDTO.builder();
        if (review.getId() != null)
            builder.id(review.getId());
        if (review.getTitle() != null && !review.getTitle().isEmpty())
            builder.title(review.getTitle());
        if (review.getContent() != null && !review.getContent().isEmpty())
            builder.content(review.getContent());

        if (review.getMember() != null && review.getMember().getName() != null && !review.getMember().getName().isEmpty())
            builder.author(review.getMember().getName());

        return builder.build();
    }

    //2. DTO -> Entity
    public Review convertToReview(ReviewDTO reviewDTO) {
        Review review = new Review();

        if (reviewDTO.getId() != null)
            review.setId(reviewDTO.getId());
        if (reviewDTO.getTitle() != null && !reviewDTO.getTitle().isEmpty()) {
            review.setTitle(reviewDTO.getTitle());
        }
        // author(name type) -> member(member type)
        if (reviewDTO.getAuthor() != null && !reviewDTO.getAuthor().isEmpty()) {
            Optional<Member> memberObj = memberRepository.findByName(reviewDTO.getAuthor());
            memberObj.ifPresent(review::setMember);
        } else {
            review.setMember(null);
        }
        if (reviewDTO.getContent() != null && !reviewDTO.getContent().isEmpty())
            review.setContent(reviewDTO.getContent());

        return review;
    }





    //리뷰목록 제공 return "post/detailPost :: #reviewList"
    String reviewList(Model model) {
        //전체 리뷰 객체를 가져옴
        List<Review> reviews = reviewRepository.findAll();

        //id 기준 정렬
        reviews = reviews.stream().sorted(Comparator.comparingLong(Review::getId)) .collect(Collectors.toList());

        //DTO생성
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        for(Review review : reviews) {
            reviewDTOs.add(getReviewDTO(review));
        }


        //DTO전달
        model.addAttribute("reviewDTOs", reviewDTOs);
        return "post/detailPost :: #reviewSection"; //리뷰 섹션(리뷰갯ㅅ, 리뷰 리스트만 갱신)
    }

    //리뷰 작성 - DB저장
    void reviewSubmit(ReviewDTO reviewDTO) {
        //DTO를 Entity로 변환
        Review review = convertToReview(reviewDTO);
        reviewRepository.save(review);
    }

    //리뷰 수정
    void editReview(ReviewDTO reviewDTO) {
        //리뷰 확인
        Review review = reviewRepository.findById(reviewDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없음."));
        //사용자 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAccountName = authentication.getName();
        if (!review.getMember().getName().equals(currentAccountName))
            throw new AccessDeniedException("리뷰 작성자가 아님.");

        //확인 됬으면 리뷰 수정
        reviewSubmit(reviewDTO);
    }
    
    //리뷰 삭제 - DB삭제
    ResponseEntity<String> deleteReview(ReviewDTO reviewDTO, Authentication auth) {
        if(auth.getName().equals(reviewDTO.getAuthor())) {
            //삭제
            reviewRepository.deleteById(reviewDTO.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("사용자가 일치 하지 않음.");
        }
    }



}
