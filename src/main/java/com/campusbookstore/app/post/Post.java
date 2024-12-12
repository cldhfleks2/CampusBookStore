package com.campusbookstore.app.post;

import com.campusbookstore.app.image.Image;
import com.campusbookstore.app.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE post SET status = 0, update_date = CURRENT_TIMESTAMP WHERE id = ?")
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String price;
    private String content;

    //남은 갯수
    private Long quantity;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ToString.Exclude
    @OneToMany(mappedBy="post", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Image> images;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //게시물 존재 여부 (1:보임 0:삭제)
    private int status = 1;
}
