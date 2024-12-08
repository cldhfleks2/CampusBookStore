package com.campusbookstore.app.post;

import com.campusbookstore.app.image.Image;
import com.campusbookstore.app.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author; //저자
    private String price;
    private String content;

    @OneToMany(mappedBy="post", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Image> images;

    @ManyToOne
    @JoinColumn(name = "member_id")
    //판매자
    private Member member;
    //게시물 존재여부 (1:보임 0:삭제)
    private int status = 1;
}
