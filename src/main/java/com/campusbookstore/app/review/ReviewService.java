package com.campusbookstore.app.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    //모든 reivew객체 얻기
    public List<Review> getReviews(){
        return reviewRepository.findAll();
    }
    
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

    String reviewList(Model model){
        List<Review> reviewObjs = reviewRepository.findAll();
        List<ReviewDTO> reviewDTOs = getReviewDTOs(reviewObjs);

        model.addAttribute("reviewDTOs", reviewDTOs);
        return "post/detailPost :: #reviewList"; //리뷰 리스트만 전송
    }
}
