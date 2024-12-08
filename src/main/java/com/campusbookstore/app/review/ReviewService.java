package com.campusbookstore.app.review;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class ReviewService {

    String reviewList(Model model){

        return "reviewList";
    }

}
