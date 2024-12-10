package com.campusbookstore.app.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/reviewList")
    String reviewList(Model model) {
        return reviewService.reviewList(model);
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


}
