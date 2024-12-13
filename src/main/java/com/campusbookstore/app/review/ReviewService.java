package com.campusbookstore.app.review;

import com.campusbookstore.app.error.ErrorService;
import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.member.MemberRepository;
import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private final PostRepository postRepository;

    //DTO를 수정하면 아래 두개를 수정해야한다.
    //1. Entity -> DTO
    @Transactional
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
        if(review.getPost() != null)
            builder.postId(review.getPost().getId());

        return builder.build();
    }

    //2. DTO -> Entity
    public Review convertToReview(ReviewDTO reviewDTO) {
        if (reviewDTO == null) return null;
        Review review = new Review();

        if (reviewDTO.getId() != null)
            review.setId(reviewDTO.getId());
        if (reviewDTO.getTitle() != null && !reviewDTO.getTitle().isEmpty()) {
            review.setTitle(reviewDTO.getTitle());
        }
        // String author -> Member member
        if (reviewDTO.getAuthor() != null && !reviewDTO.getAuthor().isEmpty()) {
            Optional<Member> memberObj = memberRepository.findByName(reviewDTO.getAuthor());
            if(!memberObj.isPresent()) throw new EntityNotFoundException("사용자 정보를 찾을 수 없습니다.");
            review.setMember(memberObj.get());
        } else {
            review.setMember(null);
        }
        if (reviewDTO.getContent() != null && !reviewDTO.getContent().isEmpty())
            review.setContent(reviewDTO.getContent());
        if(reviewDTO.getPostId() != null){
            Optional<Post> postObj = postRepository.findById(reviewDTO.getPostId());
            if(!postObj.isPresent()) throw new EntityNotFoundException("게시물 정보를 찾을 수 없습니다.");
            review.setPost(postObj.get());
        }else {
            review.setPost(null);
        }

        return review;
    }





    //리뷰목록 제공 return "post/detailPost :: #reviewList"
    @Transactional
    String reviewList(Model model) {
        //전체 리뷰 객체를 가져옴
        List<Review> reviews = reviewRepository.findAllByStatus();

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
    @Transactional
    void reviewSubmit(ReviewDTO reviewDTO) {
        //DTO를 Entity로 변환
        Review review = convertToReview(reviewDTO);
        reviewRepository.save(review);
    }

    //리뷰 수정
    @Transactional
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
    @Transactional
    ResponseEntity<String> deleteReview(ReviewDTO reviewDTO, Authentication auth) {
        if(auth.getName().equals(reviewDTO.getAuthor())) {
            //삭제
            reviewRepository.deleteById(reviewDTO.getId());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }else{
            return ErrorService.send(HttpStatus.FORBIDDEN.value(), "/deleteReview", "리뷰 작성자가 아닙니다.", ResponseEntity.class);
        }
    }



}
