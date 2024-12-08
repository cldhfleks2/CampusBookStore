package com.campusbookstore.app.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewDTO {
    private String title;
    private String author;
    private String content;
}
