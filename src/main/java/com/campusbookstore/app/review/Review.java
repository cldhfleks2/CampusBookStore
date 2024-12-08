package com.campusbookstore.app.review;

import com.campusbookstore.app.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;


    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;
    @ManyToOne
    @JoinColumn(name = "member_id")
    //작성자id
    private Member member;
    //리뷰 존재 여부 (1:보임 0:삭제)
    private int status = 1;
}
