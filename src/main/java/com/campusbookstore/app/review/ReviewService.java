package com.campusbookstore.app.review;

import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

    //DTO -> Entity
    public Review convertToReview(ReviewDTO reviewDTO) {
        Review review = new Review();

        if (reviewDTO.getTitle() != null && !reviewDTO.getTitle().isEmpty()) {
            review.setTitle(reviewDTO.getTitle());
        }
        if (reviewDTO.getContent() != null && !reviewDTO.getContent().isEmpty())
            review.setContent(reviewDTO.getContent());
        // author -> member
        if (reviewDTO.getAuthor() != null && !reviewDTO.getAuthor().isEmpty()) {
            Optional<Member> memberObj = memberRepository.findByName(reviewDTO.getAuthor());
            memberObj.ifPresent(review::setMember);
        } else {
            review.setMember(null); // Optional behavior when author is not provided
        }
        if (reviewDTO.getId() != null)
            review.setId(reviewDTO.getId());
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

    //작성한 리뷰를 DB에 저장
    //이미 존재하면 수정기능으로 작동
    void reviewSubmit(ReviewDTO reviewDTO) {
        //DTO를 Entity로 변환
        Review review = convertToReview(reviewDTO);
        reviewRepository.save(review);
    }

    void editReview(ReviewDTO reviewDTO) {
        //사용자 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAccountName = authentication.getName();
        Review review = reviewRepository.findById(reviewDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없음."));
        if (!review.getMember().getName().equals(currentAccountName))
            throw new AccessDeniedException("리뷰 작성자가 아님.");

        //확인 됬으면 리뷰 저장
        reviewSubmit(reviewDTO);
    }

    void deleteReview(ReviewDTO reviewDTO) {
        //reviewDTO쓰자

    }

}
