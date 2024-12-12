package com.campusbookstore.app.image;

import com.campusbookstore.app.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@SQLDelete(sql = "UPDATE image SET status = 0, create_date = CURRENT_TIMESTAMP WHERE id = ?")
@ToString
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imagePath;

    //만든날이자 수정한날짜(수정하는 기능은 없음)
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    //1:존재 0:(게시글삭제로인한)삭제
    private int status=1;
}
