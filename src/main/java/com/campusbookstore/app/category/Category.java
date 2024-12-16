package com.campusbookstore.app.category;

import com.campusbookstore.app.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@ToString
//같은 category.name으로 여러개가 존재할수있음 => postId여러개를 담았기 때문
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
