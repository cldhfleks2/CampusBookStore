package com.campusbookstore.app.buy;

import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE Buy SET status = 0, update_date = CURRENT_TIMESTAMP WHERE id = ?")
@ToString
public class Buy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //누가 구매?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; //무엇을 구매?

    @CreationTimestamp
    private LocalDateTime createDate;

    //구매 내역 존재 여부
    private int status = 1;
}
