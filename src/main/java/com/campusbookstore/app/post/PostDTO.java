package com.campusbookstore.app.post;

import com.campusbookstore.app.image.Image;
import com.campusbookstore.app.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PostDTO {
    private Long id;
    private String title;
    private String author;
    private String price;
    private String content;
//    private LocalDateTime createDate;
//    private LocalDateTime updateDate;

    private Member member;

    private List<MultipartFile> images;

    private List<Image> imagesEntity;
}
