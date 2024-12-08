package com.campusbookstore.app.image;

import com.campusbookstore.app.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imagePath;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    //만든날이자 수정한날짜(수정하는 기능은 없음)
    @CreationTimestamp
    private LocalDateTime date;

    //1:존재 0:(게시글삭제로인한)삭제
    private int status=1;
}
