package com.campusbookstore.app.review;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/reviewList")
    String reviewList(Model model, Long postId) {
        return reviewService.reviewList(model, postId);
    }

    @PostMapping("/reviewSubmit")
    @ResponseBody //void리턴하기위함
    void reviewSubmit(ReviewDTO reviewDTO) {
        reviewService.reviewSubmit(reviewDTO);
    }

    @PatchMapping("/editReview")
    @ResponseBody
    void editReview(ReviewDTO reviewDTO) {
        reviewService.editReview(reviewDTO); //이미 존재하면 수정하도록 JPA가 처리
    }

    @DeleteMapping("/deleteReview")
    @ResponseBody
    ResponseEntity<String> deleteReview(ReviewDTO reviewDTO, Authentication auth) {
        return reviewService.deleteReview(reviewDTO, auth);
    }




}
