package com.campusbookstore.app.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostDTO {
    private String title;
    private String author;
    private String price;
    private String content;
    private List<MultipartFile> images;

    private Long memberId;
}
