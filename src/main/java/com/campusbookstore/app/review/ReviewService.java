package com.campusbookstore.app.review;

import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    //모든 reivew객체 얻기
    public List<Review> getReviews(){
        return reviewRepository.findAll();
    }
    //DTO를 수정하면 아래 두개를 수정해야한다.
    //DTOs 얻기
    public List<ReviewDTO> getReviewDTOs(List<Review> reviewObjs) {
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        for(Review reviewObj : reviewObjs){
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .title(reviewObj.getTitle())
                    .content(reviewObj.getContent())
                    .author(reviewObj.getMember().getName())
                    .build();
            reviewDTOs.add(reviewDTO);
        }
        return reviewDTOs;
    }
    //DTO -> Entity
    public Review convertToReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setTitle(reviewDTO.getTitle());
        review.setContent(reviewDTO.getContent());
        Optional<Member> memberObj = memberRepository.findByName(reviewDTO.getAuthor());
        if(memberObj.isPresent()){
            review.setMember(memberObj.get());
        }else{
            review.setMember(null);
        }
        return review;
    }

    //작성된 리뷰들을 제공 return "post/detailPost :: #reviewList"
    String reviewList(Model model) {
        List<Review> reviewObjs = reviewRepository.findAll();
        List<ReviewDTO> reviewDTOs = getReviewDTOs(reviewObjs);

        model.addAttribute("reviewDTOs", reviewDTOs);
        return "post/detailPost :: #reviewSection"; //리뷰 섹션(리뷰갯ㅅ, 리뷰 리스트만 갱신)
    }

    //작성한 리뷰를 DB에 저장
    void reviewSubmit(ReviewDTO reviewDTO) {
        //DTO를 Entity로 변환
        Review review = convertToReview(reviewDTO);
        reviewRepository.save(review);
    }
}
