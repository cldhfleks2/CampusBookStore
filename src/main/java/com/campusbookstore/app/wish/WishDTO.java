package com.campusbookstore.app.wish;

import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WishDTO {
    private Long id;
    private Long quantity;
    private LocalDateTime createDate;
    private Member member;
    private Post post;
}
